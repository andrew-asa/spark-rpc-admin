package com.asa.spark.rpc.internalimp.env;

import com.asa.spark.rpc.internalimp.addr.RpcAddress;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public interface RpcCallContext {

    /**
     * Reply a message to the sender. If the sender is [[RpcEndpoint]], its [[RpcEndpoint.receive]]
     * will be called.
     */
    void reply(Object response);

    /**
     * Report a failure to the sender.
     */
    void sendFailure(Throwable e);

    /**
     * The sender of this message.
     */
    RpcAddress senderAddress();
}
