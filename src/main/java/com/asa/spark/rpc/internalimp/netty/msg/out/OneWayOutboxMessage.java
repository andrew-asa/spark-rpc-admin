package com.asa.spark.rpc.internalimp.netty.msg.out;

import com.asa.spark.rpc.internalimp.common.network.client.TransportClient;
import com.asa.spark.rpc.internalimp.netty.msg.OutboxMessage;

import java.nio.ByteBuffer;

/**
 * @author andrew_asa
 * @date 2018/8/8.
 */
public class OneWayOutboxMessage implements OutboxMessage {

    private ByteBuffer content;

    public OneWayOutboxMessage(ByteBuffer content) {

        this.content = content;
    }

    @Override
    public void sendWith(TransportClient client) {

        client.send(content);
    }

    @Override
    public void onFailure(Throwable e) {

    }
}
