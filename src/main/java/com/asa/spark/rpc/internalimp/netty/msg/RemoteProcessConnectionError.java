package com.asa.spark.rpc.internalimp.netty.msg;

import com.asa.spark.rpc.internalimp.addr.RpcAddress;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public class RemoteProcessConnectionError implements InboxMessage {

    private RpcAddress remoteAddress;

    private Throwable cause;

    public RpcAddress getRemoteAddress() {

        return remoteAddress;
    }

    public void setRemoteAddress(RpcAddress remoteAddress) {

        this.remoteAddress = remoteAddress;
    }

    public Throwable getCause() {

        return cause;
    }

    public void setCause(Throwable cause) {

        this.cause = cause;
    }

    @Override
    public InboxMessageType getType() {

        return InboxMessageType.RemoteProcessConnected;
    }
}
