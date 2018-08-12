package com.asa.spark.rpc.utils.internalimp.netty;

import com.asa.spark.rpc.internalimp.addr.RpcAddress;
import com.asa.spark.rpc.internalimp.netty.Dispatcher;
import com.asa.spark.rpc.internalimp.netty.Inbox;
import com.asa.spark.rpc.internalimp.netty.NettyRpcEndpointRef;
import com.asa.spark.rpc.internalimp.netty.msg.InboxMessage;
import com.asa.spark.rpc.internalimp.netty.msg.in.OneWayMessage;
import com.asa.spark.rpc.internalimp.netty.msg.in.RemoteProcessConnected;
import com.asa.spark.rpc.internalimp.netty.msg.in.RemoteProcessConnectionError;
import com.asa.spark.rpc.internalimp.netty.msg.in.RemoteProcessDisconnected;
import com.asa.spark.rpc.internalimp.netty.msg.in.RpcMessage;
import com.asa.spark.rpc.utils.CommonUtils;
import com.asa.spark.rpc.utils.SparkFunSuite;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author andrew_asa
 * @date 2018/8/12.
 */
public class InboxSuite extends SparkFunSuite {

    @Test
    public void testPost() {

        TestRpcEndpoint endpoint = new TestRpcEndpoint();
        NettyRpcEndpointRef endpointRef = mock(NettyRpcEndpointRef.class);
        Dispatcher dispatcher = Mockito.mock(Dispatcher.class);
        Inbox inbox = new Inbox(endpointRef, endpoint);
        OneWayMessage message = new OneWayMessage(null, "hi");
        inbox.post(message);
        inbox.process(dispatcher);
        CommonUtils.require(inbox.isEmpty());
        endpoint.verifySingleReceiveMessage("hi");
        inbox.stop();
        inbox.process(dispatcher);
        CommonUtils.require(inbox.isEmpty());
        endpoint.verifyStarted();
        endpoint.verifyStopped();
    }

    @Test
    public void testPostWithReply() {

        TestRpcEndpoint endpoint = new TestRpcEndpoint();
        NettyRpcEndpointRef endpointRef = mock(NettyRpcEndpointRef.class);
        Dispatcher dispatcher = mock(Dispatcher.class);

        Inbox inbox = new Inbox(endpointRef, endpoint);
        RpcMessage message = new RpcMessage(null, "hi", null);
        inbox.post(message);
        inbox.process(dispatcher);
        CommonUtils.require(inbox.isEmpty());
        endpoint.verifySingleReceiveAndReplyMessage("hi");
    }

    @Test
    public void testPostMultipleThreads() throws InterruptedException {

        TestRpcEndpoint endpoint = new TestRpcEndpoint();
        NettyRpcEndpointRef endpointRef = mock(NettyRpcEndpointRef.class);
        when(endpointRef.getName()).thenReturn("hello");

        Dispatcher dispatcher = mock(Dispatcher.class);

        AtomicInteger numDroppedMessages = new AtomicInteger(0);
        Inbox inbox = new Inbox(endpointRef, endpoint) {

            public void onDrop(InboxMessage message) {

                numDroppedMessages.incrementAndGet();
            }
        };

        CountDownLatch exitLatch = new CountDownLatch(10);

        for (int i = 0; i < 10; i++) {
            new Thread() {

                @Override
                public void run() {

                    for (int j = 0; j < 100; j++) {
                        OneWayMessage message = new OneWayMessage(null, "hi");
                        inbox.post(message);
                    }
                    exitLatch.countDown();
                }
            }.start();
        }
        // Try to process some messages
        inbox.process(dispatcher);
        inbox.stop();
        // After `stop` is called, further messages will be dropped. However, while `stop` is called,
        // some messages may be post to Inbox, so process them here.
        inbox.process(dispatcher);
        //CommonUtils.require(inbox.isEmpty());
        exitLatch.await(30, TimeUnit.SECONDS);
        CommonUtils.require(1000 == endpoint.numReceiveMessages() + numDroppedMessages.get());
        endpoint.verifyStarted();
        endpoint.verifyStopped();
    }

    @Test
    public void postAssociated() {

        TestRpcEndpoint endpoint = new TestRpcEndpoint();
        NettyRpcEndpointRef endpointRef = mock(NettyRpcEndpointRef.class);
        Dispatcher dispatcher = mock(Dispatcher.class);
        RpcAddress remoteAddress = new RpcAddress("localhost", 11111);
        Inbox inbox = new Inbox(endpointRef, endpoint);
        inbox.post(new RemoteProcessConnected(remoteAddress));
        inbox.process(dispatcher);
        endpoint.verifySingleOnConnectedMessage(remoteAddress);
    }

    @Test
    public void postDisassociated() {

        TestRpcEndpoint endpoint = new TestRpcEndpoint();
        NettyRpcEndpointRef endpointRef = mock(NettyRpcEndpointRef.class);
        Dispatcher dispatcher = mock(Dispatcher.class);
        RpcAddress remoteAddress = new RpcAddress("localhost", 11111);
        Inbox inbox = new Inbox(endpointRef, endpoint);
        inbox.post(new RemoteProcessDisconnected(remoteAddress));
        inbox.process(dispatcher);
        endpoint.verifySingleOnDisconnectedMessage(remoteAddress);
    }

    @Test
    public void postAssociationError() {

        TestRpcEndpoint endpoint = new TestRpcEndpoint();
        NettyRpcEndpointRef endpointRef = mock(NettyRpcEndpointRef.class);
        Dispatcher dispatcher = mock(Dispatcher.class);
        RpcAddress remoteAddress = new RpcAddress("localhost", 11111);
        Inbox inbox = new Inbox(endpointRef, endpoint);
        RuntimeException cause = new RuntimeException("Oops");
        inbox.post(new RemoteProcessConnectionError(cause, remoteAddress));
        inbox.process(dispatcher);
        endpoint.verifySingleOnNetworkErrorMessage(cause, remoteAddress);
    }
}
