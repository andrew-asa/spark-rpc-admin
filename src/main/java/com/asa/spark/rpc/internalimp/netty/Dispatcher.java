package com.asa.spark.rpc.internalimp.netty;

import com.asa.spark.rpc.internalimp.addr.RpcEndpointAddress;
import com.asa.spark.rpc.internalimp.endpoint.RpcEndpoint;
import com.asa.spark.rpc.internalimp.endpoint.RpcEndpointRef;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

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

    /**
     * True if the dispatcher has been stopped. Once stopped, all messages posted will be bounced
     * immediately.
     */
    private boolean stopped = false;

    private NettyRpcEnv rpcEnv;

    private int numUsableCores;

    public Dispatcher(NettyRpcEnv rpcEnv, int numUsableCores) {

        this.rpcEnv = rpcEnv;
        this.numUsableCores = numUsableCores;
    }

    public NettyRpcEndpointRef registerRpcEndpoint(String name, RpcEndpoint endpoint) {

        RpcEndpointAddress addr = new RpcEndpointAddress(rpcEnv.getAddress(), name);
        NettyRpcEndpointRef endpointRef = new NettyRpcEndpointRef(rpcEnv.getConf(), addr, rpcEnv);
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

    public void removeRpcEndpointRef(RpcEndpoint endpoint) {

        endpointRefs.remove(endpoint);
    }

}
