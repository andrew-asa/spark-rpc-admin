package com.asa.spark.rpc.utils.internalimp.netty;

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

    public void verifySingleReceiveMessage(Object message) {

        verifyReceiveMessages(CommonUtils.asList(message));
    }

    public void verifyReceiveMessages(List<Object> expected) {

        assert (CollectionUtils.isEqualCollection(expected, receiveMessages));
    }

    public void verifyStarted() {

        CommonUtils.require(started, "RpcEndpoint is not started");
    }

    public void verifyStopped() {

        CommonUtils.require(stopped, "RpcEndpoint is not stopped");
    }
}
