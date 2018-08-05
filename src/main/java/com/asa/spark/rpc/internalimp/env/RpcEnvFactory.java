package com.asa.spark.rpc.internalimp.env;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public interface RpcEnvFactory {

    RpcEnv create(RpcEnvConfig config);
}
