package com.asa.spark.rpc.internalimp.netty;

import com.asa.spark.rpc.expection.RpcEnvStoppedException;
import com.asa.spark.rpc.expection.SparkException;
import com.asa.spark.rpc.internalimp.addr.RpcEndpointAddress;
import com.asa.spark.rpc.internalimp.common.network.client.RpcResponseCallback;
import com.asa.spark.rpc.internalimp.endpoint.RpcEndpoint;
import com.asa.spark.rpc.internalimp.endpoint.RpcEndpointRef;
import com.asa.spark.rpc.internalimp.function.ExceptionFunction;
import com.asa.spark.rpc.internalimp.netty.msg.InboxMessage;
import com.asa.spark.rpc.internalimp.netty.msg.in.OneWayMessage;
import com.asa.spark.rpc.internalimp.netty.msg.in.RequestMessage;
import com.asa.spark.rpc.internalimp.netty.msg.in.RpcMessage;
import com.asa.spark.rpc.utils.CommonUtils;
import com.asa.spark.rpc.utils.StringFormatUtils;
import com.asa.spark.rpc.utils.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public class Dispatcher {

    private ConcurrentMap<String, EndpointData> endpoints = new ConcurrentHashMap<String, EndpointData>();

    private ConcurrentMap<RpcEndpoint, RpcEndpointRef> endpointRefs = new ConcurrentHashMap<RpcEndpoint, RpcEndpointRef>();

    /**
     * Track the receivers whose inboxes may contain messages.
     */
    private LinkedBlockingQueue<EndpointData> receivers = new LinkedBlockingQueue<EndpointData>();

    private static Logger LOGGER = LoggerFactory.getLogger(Dispatcher.class);

    /**
     * True if the dispatcher has been stopped. Once stopped, all messages posted will be bounced
     * immediately.
     */
    private boolean stopped = false;

    private NettyRpcEnv nettyEnv;

    private int numUsableCores;

    private ThreadPoolExecutor threadpool;

    public Dispatcher(NettyRpcEnv nettyEnv, int numUsableCores) {

        this.nettyEnv = nettyEnv;
        this.numUsableCores = numUsableCores;
        init();
    }

    private void init() {

        int availableCores = numUsableCores > 0 ? numUsableCores : Runtime.getRuntime().availableProcessors();
        int numThreads = nettyEnv.getConf().getInt("spark.rpc.netty.dispatcher.numThreads",
                                                   Math.max(2, availableCores));
        threadpool = ThreadUtils.newDaemonFixedThreadPool(numThreads, "dispatcher-event-loop");
        for (int i = 0; i < numThreads; i++) {
            threadpool.execute(new MessageLoop());
        }
    }

    public NettyRpcEndpointRef registerRpcEndpoint(String name, RpcEndpoint endpoint) {

        RpcEndpointAddress addr = new RpcEndpointAddress(nettyEnv.getAddress(), name);
        NettyRpcEndpointRef endpointRef = new NettyRpcEndpointRef(nettyEnv.getConf(), addr, nettyEnv);
        synchronized (this) {
            if (stopped) {
                throw new IllegalStateException("RpcEnv has been stopped");
            }
            if (endpoints.putIfAbsent(name, new EndpointData(name, endpoint, endpointRef)) != null) {
                throw new IllegalArgumentException("There is already an RpcEndpoint called " + name);
            }
            EndpointData data = endpoints.get(name);
            endpointRefs.put(data.getEndpoint(), data.getRef());
            // for the OnStart message
            receivers.offer(data);
        }
        return endpointRef;
    }

    public RpcEndpointRef getRpcEndpointRef(RpcEndpoint endpoint) {

        return endpointRefs.get(endpoint);
    }

    public void removeRpcEndpointRef(RpcEndpoint endpoint) {

        endpointRefs.remove(endpoint);
    }

    public void unregisterRpcEndpoint(String name) {

        EndpointData data = endpoints.remove(name);
        if (data != null) {
            data.getInbox().stop();
            // for the OnStop message
            receivers.offer(data);
        }
    }

    public synchronized void stop(RpcEndpointRef rpcEndpointRef) {

        if (stopped) {
            return;
        }
        unregisterRpcEndpoint(rpcEndpointRef.getName());
    }

    /**
     * Posts a message to a specific endpoint.
     *
     * @param endpointName      name of the endpoint.
     * @param message           the message to post
     * @param callbackIfStopped callback function if the endpoint is stopped.
     */
    private synchronized void postMessage(
            String endpointName,
            InboxMessage message,
            ExceptionFunction callbackIfStopped) {

        EndpointData data = endpoints.get(endpointName);
        Exception e = null;
        if (stopped) {
            e = new RpcEnvStoppedException();
        } else if (data == null) {
            e = new SparkException(StringFormatUtils.format("Could not find %s", endpointName));
        } else {
            data.getInbox().post(message);
            receivers.offer(data);
        }
        if (CommonUtils.allNotNull(e, callbackIfStopped)) {
            callbackIfStopped.exception(e);
        }
        // We don't need to call `onStop` in the `synchronized` block
        //error.foreach(callbackIfStopped)
    }

    public void postToAll(InboxMessage message) {

        Iterator<String> iter = endpoints.keySet().iterator();
        while (iter.hasNext()) {
            String name = iter.next();
            postMessage(name, message, new ExceptionFunction() {

                @Override
                public void exception(Exception e) {

                    LoggerFactory.getLogger(this.getClass()).error(e.getMessage(), e);
                }
            });
        }
    }

    public void postRemoteMessage(RequestMessage message, RpcResponseCallback callback) {

        RemoteNettyRpcCallContext rpcCallContext = new RemoteNettyRpcCallContext(nettyEnv, callback, message.getSenderAddress());
        RpcMessage rpcMessage = new RpcMessage(message.getSenderAddress(), message.getContent(), rpcCallContext);
        postMessage(message.getReceiver().getName(), rpcMessage, new ExceptionFunction() {

            @Override
            public void exception(Exception o) {

                callback.onFailure(o);
            }
        });
    }

    public void postOneWayMessage(RequestMessage message) {

        postMessage(message.getReceiver().getName(), new OneWayMessage(message.getSenderAddress(), message.getContent()),
                    null);
    }

    private class MessageLoop implements Runnable {

        @Override
        public void run() {

            try {
                while (true) {
                    EndpointData data = receivers.take();
                    if (PoisonPill.equals(data)) {
                        // Put PoisonPill back so that other MessageLoops can see it.
                        receivers.offer(PoisonPill);
                        return;
                    }
                    data.getInbox().process(Dispatcher.this);
                }
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    private EndpointData PoisonPill = new EndpointData(null, null, null);
}
