package com.asa.spark.rpc.internalimp.netty;

import com.asa.spark.rpc.internalimp.env.RpcCallContext;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public abstract class NettyRpcCallContext implements RpcCallContext {

    protected abstract void send(Object message);


    @Override
    public void reply(Object response) {

        send(response);
    }

    @Override
    public void sendFailure(Throwable e) {

        send(new RpcFailure(e));
    }
}
