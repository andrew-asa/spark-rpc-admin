package com.asa.spark.rpc.internalimp.env;

import com.asa.spark.rpc.internalimp.conf.SparkConf;
import com.asa.spark.rpc.internalimp.serializer.Serializer;
import com.asa.spark.rpc.internalimp.serializer.SerializerManager;

/**
 * @author andrew_asa
 * @date 2018/8/12.
 */
public class SparkEnv {

    private SparkEnv env;

    String executorId;

    private RpcEnv rpcEnv;

    private Serializer serializer;

    Serializer closureSerializer;

    SerializerManager serializerManager;

    //MapOutputTracker mapOutputTracker;

    //ShuffleManager shuffleManager;

    //BroadcastManager broadcastManager;

    //BlockManager blockManager;

    SecurityManager securityManager;

    //MetricsSystem metricsSystem;

    //MemoryManager memoryManager;

    //OutputCommitCoordinator;

    SparkConf conf;

    public String getExecutorId() {

        return executorId;
    }

    public void setExecutorId(String executorId) {

        this.executorId = executorId;
    }

    public RpcEnv getRpcEnv() {

        return rpcEnv;
    }

    public void setRpcEnv(RpcEnv rpcEnv) {

        this.rpcEnv = rpcEnv;
    }

    public Serializer getSerializer() {

        return serializer;
    }

    public void setSerializer(Serializer serializer) {

        this.serializer = serializer;
    }

    public Serializer getClosureSerializer() {

        return closureSerializer;
    }

    public void setClosureSerializer(Serializer closureSerializer) {

        this.closureSerializer = closureSerializer;
    }

    public SerializerManager getSerializerManager() {

        return serializerManager;
    }

    public void setSerializerManager(SerializerManager serializerManager) {

        this.serializerManager = serializerManager;
    }

    public SecurityManager getSecurityManager() {

        return securityManager;
    }

    public void setSecurityManager(SecurityManager securityManager) {

        this.securityManager = securityManager;
    }

    public SparkConf getConf() {

        return conf;
    }

    public void setConf(SparkConf conf) {

        this.conf = conf;
    }

    public SparkEnv getEnv() {

        return env;
    }

    public void setEnv(SparkEnv env) {

        this.env = env;
    }
}
