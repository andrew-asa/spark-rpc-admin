package com.asa.spark.rpc.internalimp.netty.msg;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public enum InboxMessageType {

    OnStart(1),
    OnStop(2),
    RemoteProcessConnected(3),
    RemoteProcessConnectionError(4),
    RemoteProcessDisconnected(5),
    RpcMessage(6),
    OneWayMessage(7);

    InboxMessageType(int type) {

        this.type = type;
    }

    private int type;
}
