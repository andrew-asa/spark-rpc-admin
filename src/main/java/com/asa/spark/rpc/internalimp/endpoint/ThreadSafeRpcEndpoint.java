package com.asa.spark.rpc.internalimp.endpoint;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */

/**
 * A trait that requires RpcEnv thread-safely sending messages to it.
 * <p>
 * Thread-safety means processing of one message happens before processing of the next message by
 * the same [[ThreadSafeRpcEndpoint]]. In the other words, changes to internal fields of a
 * [[ThreadSafeRpcEndpoint]] are visible when processing the next message, and fields in the
 * [[ThreadSafeRpcEndpoint]] need not be volatile or equivalent.
 * <p>
 * However, there is no guarantee that the same thread will be executing the same
 * [[ThreadSafeRpcEndpoint]] for different messages.
 */
public class ThreadSafeRpcEndpoint extends RpcEndpoint {

}
