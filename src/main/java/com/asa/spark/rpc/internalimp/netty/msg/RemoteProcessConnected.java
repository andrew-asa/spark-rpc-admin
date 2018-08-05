package com.asa.spark.rpc.internalimp.netty.msg;

import com.asa.spark.rpc.internalimp.addr.RpcAddress;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public class RemoteProcessConnected implements InboxMessage {

    private RpcAddress remoteAddress;

    public RpcAddress getRemoteAddress() {

        return remoteAddress;
    }

    public void setRemoteAddress(RpcAddress remoteAddress) {

        this.remoteAddress = remoteAddress;
    }

    @Override
    public InboxMessageType getType() {

        return InboxMessageType.RemoteProcessConnectionError;
    }
}
