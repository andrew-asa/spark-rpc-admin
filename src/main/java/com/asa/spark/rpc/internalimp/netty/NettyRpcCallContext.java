package com.asa.spark.rpc.internalimp.netty;

import com.asa.spark.rpc.internalimp.addr.RpcAddress;
import com.asa.spark.rpc.internalimp.env.RpcCallContext;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public class NettyRpcCallContext extends RpcAddress implements RpcCallContext {

    public NettyRpcCallContext(String host, int port) {

        super(host, port);
    }

    @Override
    public void reply(Object response) {

    }

    @Override
    public void sendFailure(Throwable e) {

    }

    @Override
    public RpcAddress senderAddress() {

        return null;
    }
}
