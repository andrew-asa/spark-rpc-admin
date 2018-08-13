package com.asa.spark.rpc.utils.internalimp.netty;

import com.asa.spark.rpc.internalimp.conf.SparkConf;
import com.asa.spark.rpc.internalimp.env.RpcEnv;
import com.asa.spark.rpc.utils.internalimp.RpcEnvSuite;

/**
 * @author andrew_asa
 * @date 2018/8/12.
 */
public class NettyRpcEnvSuite extends RpcEnvSuite {

    @Override
    public RpcEnv createRpcEnv(SparkConf conf, String name, int port, boolean clientModel) {

        return null;
    }
}
