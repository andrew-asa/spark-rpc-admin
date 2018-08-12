package com.asa.spark.rpc.internalimp.netty;

import com.asa.spark.rpc.internalimp.endpoint.RpcEndpoint;
import com.asa.spark.rpc.internalimp.env.RpcCallContext;
import com.asa.spark.rpc.internalimp.env.RpcEnv;

/**
 * @author andrew_asa
 * @date 2018/8/12.
 */
public class RpcEndpointVerifier extends RpcEndpoint {

    private RpcEnv rpcEnv;

    private Dispatcher dispatcher;

    public static final String NAME = "endpoint-verifier";

    public RpcEndpointVerifier(RpcEnv rpcEnv, Dispatcher dispatcher) {

        this.rpcEnv = rpcEnv;
        this.dispatcher = dispatcher;
    }

    //public PartialFunction receiveAndReply(RpcCallContext context) {
    //    if(CheckExistence())
    //    case RpcEndpointVerifier.CheckExistence(name) => context.reply(dispatcher.verify(name))
    //}

    public boolean CheckExistence(String name) {
        return true;
    }
}
