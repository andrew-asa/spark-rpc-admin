package com.asa.spark.rpc.internalimp.netty.msg;

import com.asa.spark.rpc.internalimp.common.network.client.TransportClient;

/**
 * @author andrew_asa
 * @date 2018/8/8.
 */
public interface OutboxMessage {

    void sendWith(TransportClient client);

    void onFailure(Throwable e);
}
