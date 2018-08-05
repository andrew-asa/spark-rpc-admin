package com.asa.spark.rpc.internalimp.netty.msg;

import com.asa.spark.rpc.internalimp.addr.RpcAddress;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public class OneWayMessage implements InboxMessage {

    private RpcAddress senderAddress;

    private Object content;

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

    @Override
    public InboxMessageType getType() {

        return InboxMessageType.OneWayMessage;
    }
}
