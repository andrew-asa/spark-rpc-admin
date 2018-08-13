package com.asa.spark.rpc.utils.internalimp.netty;

import com.asa.spark.rpc.internalimp.addr.RpcAddress;
import com.asa.spark.rpc.internalimp.common.network.data.Pair;
import com.asa.spark.rpc.internalimp.endpoint.ThreadSafeRpcEndpoint;
import com.asa.spark.rpc.utils.CommonUtils;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author andrew_asa
 * @date 2018/8/12.
 */
public class TestRpcEndpoint extends ThreadSafeRpcEndpoint {

    private List<Object> receiveMessages = new ArrayList<Object>();

    private List<Object> receiveAndReplyMessages = new ArrayList<Object>();

    private List<RpcAddress> onConnectedMessages = new ArrayList<RpcAddress>();

    private List<RpcAddress> onDisconnectedMessages = new ArrayList<RpcAddress>();

    private List<Pair<Throwable, RpcAddress>> onNetworkErrorMessages = new ArrayList<Pair<Throwable, RpcAddress>>();

    private boolean started = false;

    private boolean stopped = false;

    @Override
    public void onStart() {

        started = true;
    }

    @Override
    public void onStop() {

        stopped = true;
    }

    /**
     * Process messages from `RpcEndpointRef.send` or `RpcCallContext.reply`. If receiving a
     * unmatched message, `SparkException` will be thrown and sent to `onError`.
     */
    public void receive(Object content) {

        receiveMessages.add(content);
    }

    public void receiveAndReply(Object context) {

        receiveAndReplyMessages.add(context);
    }

    public void onConnected(RpcAddress remoteAddress) {

        onConnectedMessages.add(remoteAddress);
    }

    public void onNetworkError(Throwable cause, RpcAddress remoteAddress) {

        onNetworkErrorMessages.add(new Pair<Throwable, RpcAddress>(cause, remoteAddress));
    }

    public void onDisconnected(RpcAddress remoteAddress) {

        onDisconnectedMessages.add(remoteAddress);
    }

    public void verifySingleReceiveMessage(Object message) {

        verifyReceiveMessages(CommonUtils.asList(message));
    }

    public void verifySingleReceiveAndReplyMessage(Object message) {

        verifyReceiveAndReplyMessages(CommonUtils.asList(message));
    }

    public void verifyReceiveAndReplyMessages(List<Object> expected) {

        assert (CollectionUtils.isEqualCollection(expected, receiveAndReplyMessages));

    }

    public void verifyReceiveMessages(List<Object> expected) {

        assert (CollectionUtils.isEqualCollection(expected, receiveMessages));
    }

    public void verifyOnConnectedMessages(List<RpcAddress> expected) {

        CommonUtils.require(CollectionUtils.isEqualCollection(expected,onConnectedMessages ));
    }

    public void verifySingleOnConnectedMessage(RpcAddress remoteAddress) {

        verifyOnConnectedMessages(CommonUtils.asList(remoteAddress));
    }

    public void verifyOnDisconnectedMessages(List<RpcAddress> expected) {

        CommonUtils.require(CollectionUtils.isEqualCollection(expected, onDisconnectedMessages));
    }

    public void verifySingleOnDisconnectedMessage(RpcAddress remoteAddress) {

        verifyOnDisconnectedMessages(CommonUtils.asList(remoteAddress));
    }

    public void verifyOnNetworkErrorMessages(List<Pair<Throwable, RpcAddress>> expected) {

        CommonUtils.require(CollectionUtils.isEqualCollection(expected, onNetworkErrorMessages));
    }

    public void verifySingleOnNetworkErrorMessage(Throwable cause, RpcAddress remoteAddress) {

        verifyOnNetworkErrorMessages(CommonUtils.asList(new Pair<Throwable, RpcAddress>(cause, remoteAddress)));
    }

    public void verifyStarted() {

        CommonUtils.require(started, "RpcEndpoint is not started");
    }

    public void verifyStopped() {

        CommonUtils.require(stopped, "RpcEndpoint is not stopped");
    }

    public int numReceiveMessages() {

        return receiveMessages.size();
    }
}
