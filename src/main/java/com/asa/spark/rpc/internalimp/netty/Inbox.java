package com.asa.spark.rpc.internalimp.netty;

import com.asa.spark.rpc.internalimp.endpoint.RpcEndpoint;
import com.asa.spark.rpc.internalimp.endpoint.ThreadSafeRpcEndpoint;
import com.asa.spark.rpc.internalimp.netty.msg.InboxMessage;
import com.asa.spark.rpc.internalimp.netty.msg.OnStart;
import com.asa.spark.rpc.internalimp.netty.msg.OnStop;
import com.asa.spark.rpc.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public class Inbox {

    private LinkedList<InboxMessage> messages = new LinkedList<InboxMessage>();

    private NettyRpcEndpointRef endpointRef;

    private RpcEndpoint endpoint;

    private boolean stopped = false;

    /**
     * Allow multiple threads to process messages at the same time.
     */
    private boolean enableConcurrent = false;

    /**
     * The number of threads processing messages for this inbox.
     */
    private int numActiveThreads = 0;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public Inbox(NettyRpcEndpointRef endpointRef, RpcEndpoint endpoint) {

        this.endpointRef = endpointRef;
        this.endpoint = endpoint;
        init();
    }

    private synchronized void init() {

        messages.add(new OnStart());
    }

    public void process(Dispatcher dispatcher) {

        InboxMessage message = null;
        synchronized (this) {
            if (!enableConcurrent && numActiveThreads != 0) {
                return;
            }
            message = messages.poll();
            if (message != null) {
                numActiveThreads += 1;
            } else {
                return;
            }
        }
        while (true) {
            switch (message.getType()) {
                case OnStart: {
                    endpoint.onStart();
                    if (endpoint instanceof ThreadSafeRpcEndpoint) {
                        synchronized (this) {
                            if (!stopped) {
                                enableConcurrent = true;
                            }
                        }
                    }
                    break;
                }
                case OnStop: {
                    int activeThreads;
                    synchronized (this) {
                        activeThreads = numActiveThreads;
                    }
                    CommonUtils.require(activeThreads == 1,
                                        "There should be only a single active thread but found " + activeThreads + "threads.");
                    dispatcher.removeRpcEndpointRef(endpoint);
                    endpoint.onStop();
                    //assert(isEmpty, "OnStop should be the last message")
                    break;
                }
                case RemoteProcessConnected: {

                    break;
                }
                case RemoteProcessConnectionError: {

                    break;
                }
                case RemoteProcessDisconnected: {

                    break;
                }
                case RpcMessage: {

                    break;
                }
                case OneWayMessage: {

                    break;
                }
                default:
            }
        }
    }

    public void post(InboxMessage message) {

        if (stopped) {
            // We already put "OnStop" into "messages", so we should drop further messages
            onDrop(message);
        } else {
            messages.add(message);
        }
    }

    protected void onDrop(InboxMessage message) {

        LOGGER.warn("Drop $message because $endpointRef is stopped");
    }

    public synchronized void stop() {
        // The following codes should be in `synchronized` so that we can make sure "OnStop" is the last
        // message
        if (!stopped) {
            // We should disable concurrent here. Then when RpcEndpoint.onStop is called, it's the only
            // thread that is processing messages. So `RpcEndpoint.onStop` can release its resources
            // safely.
            enableConcurrent = false;
            stopped = true;
            messages.add(new OnStop());
            // Note: The concurrent events in messages will be processed one by one.
        }
    }

    public synchronized boolean isEmpty() {

        return messages.isEmpty();
    }
}
