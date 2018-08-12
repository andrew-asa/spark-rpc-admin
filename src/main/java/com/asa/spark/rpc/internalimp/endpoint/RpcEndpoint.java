package com.asa.spark.rpc.internalimp.endpoint;

import com.asa.spark.rpc.expection.SparkException;
import com.asa.spark.rpc.internalimp.addr.RpcAddress;
import com.asa.spark.rpc.internalimp.env.RpcEnv;
import com.asa.spark.rpc.utils.CommonUtils;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 * 终端
 */
public abstract class RpcEndpoint {

    private RpcEnv rpcEnv;

    public void setRpcEnv(RpcEnv rpcEnv) {

        this.rpcEnv = rpcEnv;
    }

    public RpcEnv getRpcEnv() {

        return rpcEnv;
    }

    /**
     * The [[RpcEndpointRef]] of this [[RpcEndpoint]]. `self` will become valid when `onStart` is
     * called. And `self` will become `null` when `onStop` is called.
     * <p>
     * Note: Because before `onStart`, [[RpcEndpoint]] has not yet been registered and there is not
     * valid [[RpcEndpointRef]] for it. So don't call `self` before `onStart` is called.
     */
    public final RpcEndpointRef self() {

        CommonUtils.require(rpcEnv != null, "rpcEnv has not been initialized");
        return rpcEnv.endpointRef(this);
    }

    /**
     * Process messages from `RpcEndpointRef.send` or `RpcCallContext.reply`. If receiving a
     * unmatched message, `SparkException` will be thrown and sent to `onError`.
     */
    public void receive(Object content) {

    }

    /**
     * Process messages from `RpcEndpointRef.ask`. If receiving a unmatched message,
     * `SparkException` will be thrown and sent to `onError`.
     */
    //def receiveAndReply(context:RpcCallContext): PartialFunction[Any, Unit] = {
    //    case _ => context.sendFailure(new SparkException(self + " won't reply anything"))
    //}

    /**
     * Invoked when any exception is thrown during handling messages.
     */
    public void onError(Throwable cause) {
        // By default, throw e and let RpcEnv handle it
        //throw cause;
    }

    /**
     * Invoked when `remoteAddress` is connected to the current node.
     */
    public void onConnected(RpcAddress remoteAddress) {
        // By default, do nothing.
    }

    /**
     * Invoked when `remoteAddress` is lost.
     */
    public void onDisconnected(RpcAddress remoteAddress) {
        // By default, do nothing.
    }

    /**
     * Invoked when some network error happens in the connection between the current node and
     * `remoteAddress`.
     */
    public void onNetworkError(Throwable cause, RpcAddress remoteAddress) {
        // By default, do nothing.
    }

    /**
     * Invoked before [[RpcEndpoint]] starts to handle any message.
     */
    public void onStart() {
        // By default, do nothing.
    }

    /**
     * Invoked when [[RpcEndpoint]] is stopping. `self` will be `null` in this method and you cannot
     * use it to send or ask messages.
     */
    public void onStop() {
        // By default, do nothing.
    }

    /**
     * A convenient method to stop [[RpcEndpoint]].
     */
    public final void stop() {

        RpcEndpointRef _self = this.self();
        if (_self != null) {
            rpcEnv.stop(_self);
        }
    }
}
