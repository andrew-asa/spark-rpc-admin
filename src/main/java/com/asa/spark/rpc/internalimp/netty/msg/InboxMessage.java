package com.asa.spark.rpc.internalimp.netty.msg;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public interface InboxMessage {

    InboxMessageType getType();
}
