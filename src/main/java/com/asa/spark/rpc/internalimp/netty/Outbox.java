package com.asa.spark.rpc.internalimp.netty;

import com.asa.spark.rpc.expection.SparkException;
import com.asa.spark.rpc.internalimp.addr.RpcAddress;
import com.asa.spark.rpc.internalimp.common.network.client.TransportClient;
import com.asa.spark.rpc.internalimp.netty.msg.OutboxMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @author andrew_asa
 * @date 2018/8/8.
 */
public class Outbox {

    private NettyRpcEnv nettyEnv;

    private RpcAddress address;

    private LinkedList<OutboxMessage> messages = new LinkedList<OutboxMessage>();

    private TransportClient client;

    private Future connectFuture;

    private boolean stopped = false;

    private boolean draining = false;

    private NettyStreamManager streamManager;

    Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public Outbox(NettyRpcEnv nettyEnv, RpcAddress address) {

        this.nettyEnv = nettyEnv;
        this.address = address;
        init();
    }

    private void init() {

        streamManager = new NettyStreamManager(nettyEnv);
    }

    /**
     * Send a message. If there is no active connection, cache it and launch a new connection. If
     * [[Outbox]] is stopped, the sender will be notified with a [[SparkException]].
     */
    public synchronized void send(OutboxMessage message) {

        boolean dropped = false;
        if (stopped) {
            dropped = true;
        } else {
            messages.add(message);
        }
        if (dropped) {
            message.onFailure(new SparkException("Message is dropped because Outbox is stopped"));
        } else {
            drainOutbox();
        }
    }

    /**
     * Drain the message queue. If there is other draining thread, just exit. If the connection has
     * not been established, launch a task in the `nettyEnv.clientConnectionExecutor` to setup the
     * connection.
     */
    private void drainOutbox() {

        OutboxMessage message;
        synchronized (this) {
            if (stopped) {
                return;
            }
            if (connectFuture != null) {
                // We are connecting to the remote address, so just exit
                return;
            }
            if (client == null) {
                // There is no connect task but client is null, so we need to launch the connect task.
                launchConnectTask();
                return;
            }
            if (draining) {
                // There is some thread draining, so just exit
                return;
            }
            message = messages.poll();
            if (message == null) {
                return;
            }
            draining = true;
        }
        while (true) {
            try {
                message.sendWith(client);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                return;

            }
            if (stopped) {
                return;
            }
            message = messages.poll();
            if (message == null) {
                draining = false;
                return;
            }
        }
    }

    private void launchConnectTask() {

        connectFuture = nettyEnv.getClientConnectionExecutor().submit(new Callable<Object>() {

            @Override
            public Object call() throws Exception {

                TransportClient _client = nettyEnv.createClient(address);
                synchronized (Outbox.this) {
                    client = _client;
                    if (stopped) {
                        closeClient();
                    }
                }
                drainOutbox();
                return null;
            }
        });
    }

    /**
     * Stop [[Inbox]] and notify the waiting messages with the cause.
     */
    private void handleNetworkFailure(Throwable e) {

        synchronized (this) {
            assert (connectFuture == null);
            if (stopped) {
                return;
            }
            stopped = true;
            closeClient();
        }
        // Remove this Outbox from nettyEnv so that the further messages will create a new Outbox along
        // with a new connection
        nettyEnv.removeOutbox(address);

        // Notify the connection failure for the remaining messages
        //
        // We always check `stopped` before updating messages, so here we can make sure no thread will
        // update messages and it's safe to just drain the queue.
        OutboxMessage message = messages.poll();
        while (message != null) {
            message.onFailure(e);
            message = messages.poll();
        }
        assert (messages.isEmpty());
    }

    private synchronized void closeClient() {
        // Just set client to null. Don't close it in order to reuse the connection.
        client = null;
    }

    /**
     * Stop [[Outbox]]. The remaining messages in the [[Outbox]] will be notified with a
     * [[SparkException]].
     */
    public void stop() {

        synchronized (this) {
            if (stopped) {
                return;
            }
            stopped = true;
            if (connectFuture != null) {
                connectFuture.cancel(true);
            }
            closeClient();
        }

        // We always check `stopped` before updating messages, so here we can make sure no thread will
        // update messages and it's safe to just drain the queue.
        OutboxMessage message = messages.poll();
        while (message != null) {
            message.onFailure(new SparkException("Message is dropped because Outbox is stopped"));
            message = messages.poll();
        }
    }
}
