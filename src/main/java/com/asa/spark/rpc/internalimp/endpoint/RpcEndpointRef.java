package com.asa.spark.rpc.internalimp.endpoint;

import com.asa.spark.rpc.internalimp.addr.RpcAddress;
import com.asa.spark.rpc.internalimp.conf.SparkConf;
import com.asa.spark.rpc.utils.RpcUtils;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 * 终端索引
 */
public abstract class RpcEndpointRef {

    private int maxRetries = RpcUtils.numRetries(SparkConf.getInstants());

    private long retryWaitMs = RpcUtils.retryWaitMs(SparkConf.getInstants());
    //private long defaultAskTimeout = RpcUtils.askRpcTimeout(conf);

    /**
     * return the address for the [[RpcEndpointRef]]
     */
    private RpcAddress address;

    private String name;

    /**
     * Sends a one-way asynchronous message. Fire-and-forget semantics.
     */
    public abstract void send(Object message);

    /**
     * Send a message to the corresponding [[RpcEndpoint.receiveAndReply)]] and return a [[Future]] to
     * receive the reply within the specified timeout.
     *
     * This method only sends the message once and never retries.
     */
    //def ask[T: ClassTag](message: Any, timeout: RpcTimeout): Future[T]

    /**
     * Send a message to the corresponding [[RpcEndpoint.receiveAndReply)]] and return a [[Future]] to
     * receive the reply within a default timeout.
     *
     * This method only sends the message once and never retries.
     */
    //def ask[T: ClassTag](message: Any): Future[T] = ask(message, defaultAskTimeout)

    /**
     * Send a message to the corresponding [[RpcEndpoint.receiveAndReply]] and get its result within a
     * default timeout, throw an exception if this fails.
     *
     * Note: this is a blocking action which may cost a lot of time,  so don't call it in a message
     * loop of [[RpcEndpoint]].

     * @param message the message to send
     * @tparam T type of the reply message
     * @return the reply message from the corresponding [[RpcEndpoint]]
     */
    //def askSync[T: ClassTag](message: Any): T = askSync(message, defaultAskTimeout)

    /**
     * Send a message to the corresponding [[RpcEndpoint.receiveAndReply]] and get its result within a
     * specified timeout, throw an exception if this fails.
     *
     * Note: this is a blocking action which may cost a lot of time, so don't call it in a message
     * loop of [[RpcEndpoint]].
     *
     * @param message the message to send
     * @param timeout the timeout duration
     * @tparam T type of the reply message
     * @return the reply message from the corresponding [[RpcEndpoint]]
     */
    //def askSync[T: ClassTag](message: Any, timeout: RpcTimeout): T = {
    //    val future = ask[T](message, timeout)
    //    timeout.awaitResult(future)
    //}
}
