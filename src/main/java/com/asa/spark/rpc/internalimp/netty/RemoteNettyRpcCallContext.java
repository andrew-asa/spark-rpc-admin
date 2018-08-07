package com.asa.spark.rpc.internalimp.netty;


import com.asa.spark.rpc.internalimp.addr.RpcAddress;
import com.asa.spark.rpc.internalimp.common.network.client.RpcResponseCallback;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * @author andrew_asa
 * @date 2018/8/7.
 */
public class RemoteNettyRpcCallContext extends NettyRpcCallContext {

    private NettyRpcEnv nettyEnv;

    private RpcResponseCallback callback;

    private RpcAddress senderAddress;

    public RemoteNettyRpcCallContext(NettyRpcEnv nettyEnv, RpcResponseCallback callback, RpcAddress senderAddress) {

        this.nettyEnv = nettyEnv;
        this.callback = callback;
        this.senderAddress = senderAddress;
    }

    public NettyRpcEnv getNettyEnv() {

        return nettyEnv;
    }

    public void setNettyEnv(NettyRpcEnv nettyEnv) {

        this.nettyEnv = nettyEnv;
    }

    public RpcResponseCallback getCallback() {

        return callback;
    }

    public void setCallback(RpcResponseCallback callback) {

        this.callback = callback;
    }

    public RpcAddress getSenderAddress() {

        return senderAddress;
    }

    public void setSenderAddress(RpcAddress senderAddress) {

        this.senderAddress = senderAddress;
    }

    @Override
    protected void send(Object message) {

        try {
            ByteBuffer buffer = nettyEnv.serialize(message);
            callback.onSuccess(buffer);
        } catch (Exception e) {
            LoggerFactory.getLogger(this.getClass()).error(e.getMessage(), e);
            //callback.onFailure(e);
        }
    }

    @Override
    public RpcAddress senderAddress() {

        return senderAddress;
    }
}
