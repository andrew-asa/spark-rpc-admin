package com.asa.spark.rpc.utils.internalimp.netty;

import com.asa.spark.rpc.internalimp.netty.Dispatcher;
import com.asa.spark.rpc.internalimp.netty.Inbox;
import com.asa.spark.rpc.internalimp.netty.NettyRpcEndpointRef;
import com.asa.spark.rpc.internalimp.netty.msg.in.OneWayMessage;
import com.asa.spark.rpc.utils.CommonUtils;
import com.asa.spark.rpc.utils.SparkFunSuite;
import org.junit.Test;
import org.mockito.Mockito;

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
}
