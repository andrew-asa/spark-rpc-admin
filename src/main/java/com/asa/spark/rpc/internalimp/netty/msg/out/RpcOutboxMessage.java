package com.asa.spark.rpc.internalimp.netty.msg.out;

import com.asa.spark.rpc.internalimp.common.network.client.TransportClient;
import com.asa.spark.rpc.internalimp.netty.msg.OutboxMessage;

/**
 * @author andrew_asa
 * @date 2018/8/8.
 */
public class RpcOutboxMessage implements OutboxMessage {

    @Override
    public void sendWith(TransportClient client) {

    }

    @Override
    public void onFailure(Throwable e) {

    }
}
