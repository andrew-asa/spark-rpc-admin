package com.asa.spark.rpc.internalimp.netty.msg.in;

import com.asa.spark.rpc.internalimp.netty.msg.InboxMessage;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public class OnStop implements InboxMessage {

    @Override
    public InboxMessageType getType() {

        return InboxMessageType.OnStop;
    }
}
