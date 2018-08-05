package com.asa.spark.rpc.internalimp.netty;

import com.asa.spark.rpc.expection.SparkException;
import com.asa.spark.rpc.internalimp.endpoint.RpcEndpoint;
import com.asa.spark.rpc.internalimp.endpoint.ThreadSafeRpcEndpoint;
import com.asa.spark.rpc.internalimp.netty.msg.InboxMessage;
import com.asa.spark.rpc.internalimp.netty.msg.InboxMessageType;
import com.asa.spark.rpc.internalimp.netty.msg.OnStart;
import com.asa.spark.rpc.internalimp.netty.msg.RpcMessage;
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
}
