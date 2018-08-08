package com.asa.spark.rpc.internalimp.netty.msg.in;

import com.asa.spark.rpc.internalimp.addr.RpcAddress;
import com.asa.spark.rpc.internalimp.netty.NettyRpcCallContext;
import com.asa.spark.rpc.internalimp.netty.msg.InboxMessage;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public class RpcMessage implements InboxMessage {

    private RpcAddress senderAddress;

    private Object content;

    private NettyRpcCallContext context;

    public RpcMessage(RpcAddress senderAddress, Object content, NettyRpcCallContext context) {

        this.senderAddress = senderAddress;
        this.content = content;
        this.context = context;
    }

    public RpcAddress getSenderAddress() {

        return senderAddress;
    }

    public void setSenderAddress(RpcAddress senderAddress) {

        this.senderAddress = senderAddress;
    }

    public Object getContent() {

        return content;
    }

    public void setContent(Object content) {

        this.content = content;
    }

    public NettyRpcCallContext getContext() {

        return context;
    }

    public void setContext(NettyRpcCallContext context) {

        this.context = context;
    }

    @Override
    public InboxMessageType getType() {

        return InboxMessageType.RpcMessage;
    }
}
