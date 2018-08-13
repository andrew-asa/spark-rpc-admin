package com.asa.spark.rpc.internalimp.netty;

import com.asa.spark.rpc.internalimp.conf.SparkConf;
import com.asa.spark.rpc.internalimp.env.RpcEnv;
import com.asa.spark.rpc.internalimp.env.RpcEnvConfig;
import com.asa.spark.rpc.internalimp.env.RpcEnvFactory;
import com.asa.spark.rpc.internalimp.serializer.JavaSerializer;
import com.asa.spark.rpc.internalimp.serializer.JavaSerializerInstance;

/**
 * @author andrew_asa
 * @date 2018/8/12.
 */
public class NettyRpcEnvFactory implements RpcEnvFactory {

    @Override
    public RpcEnv create(RpcEnvConfig config) {
        SparkConf sparkConf = config.getConf();
        // Use JavaSerializerInstance in multiple threads is safe. However, if we plan to support
        // KryoSerializer in future, we have to use ThreadLocal to store SerializerInstance
        JavaSerializerInstance javaSerializerInstance =
                (JavaSerializerInstance)new JavaSerializer(sparkConf).newInstance();
        NettyRpcEnv nettyEnv =
                new NettyRpcEnv(sparkConf, javaSerializerInstance, config.getAdvertiseAddress(),
                                config.getSecurityManager(), config.getNumUsableCores());
        if (!config.isClientMode()) {
           nettyEnv.startServer(config.getBindAddress(),nettyEnv.getAddress().getPort());

        }
        return nettyEnv    ;
    }
}
