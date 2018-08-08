package com.asa.spark.rpc.internalimp.netty.msg;

import com.asa.spark.rpc.internalimp.netty.msg.in.InboxMessageType;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public interface InboxMessage {

    InboxMessageType getType();
}
