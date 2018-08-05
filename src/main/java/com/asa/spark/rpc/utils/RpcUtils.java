package com.asa.spark.rpc.utils;

import com.asa.spark.rpc.internalimp.conf.SparkConf;
import com.asa.spark.rpc.internalimp.endpoint.RpcEndpointRef;
import com.asa.spark.rpc.internalimp.env.RpcEnv;
import com.asa.spark.rpc.internalimp.env.RpcTimeout;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public class RpcUtils {

    /**
     * Retrieve a `RpcEndpointRef` which is located in the driver via its name.
     */
    //def makeDriverRef(name: String, conf:SparkConf, rpcEnv:RpcEnv): RpcEndpointRef = {
    //    val driverHost: String = conf.get("spark.driver.host", "localhost")
    //    val driverPort: Int = conf.getInt("spark.driver.port", 7077)
    //    Utils.checkHost(driverHost)
    //    rpcEnv.setupEndpointRef(RpcAddress(driverHost, driverPort), name)
    //}

    /**
     * Returns the configured number of times to retry connecting
     */
    public static int numRetries(SparkConf conf) {

        return conf.getInt("spark.rpc.numRetries", 3);
    }

    /**
     * Returns the configured number of milliseconds to wait on each retry
     */
    public static long retryWaitMs(SparkConf conf) {

        return conf.getTimeAsMs("spark.rpc.retry.wait", "3s");
    }

    /** Returns the default Spark timeout to use for RPC ask operations. */
    //def askRpcTimeout(conf: SparkConf): RpcTimeout = {
    //    RpcTimeout(conf, Seq("spark.rpc.askTimeout", "spark.network.timeout"), "120s")
    //}

    /**
     * Returns the default Spark timeout to use for RPC remote endpoint lookup.
     */
    //def lookupRpcTimeout(conf: SparkConf): RpcTimeout = {
    //    RpcTimeout(conf, Seq("spark.rpc.lookupTimeout", "spark.network.timeout"), "120s")
    //}

    private int MAX_MESSAGE_SIZE_IN_MB = Integer.MAX_VALUE / 1024 / 1024;

    /** Returns the configured max message size for messages in bytes. */
    //def maxMessageSizeBytes(conf: SparkConf): Int = {
    //    val maxSizeInMB = conf.getInt("spark.rpc.message.maxSize", 128)
    //    if (maxSizeInMB > MAX_MESSAGE_SIZE_IN_MB) {
    //        throw new IllegalArgumentException(
    //                s"spark.rpc.message.maxSize should not be greater than $MAX_MESSAGE_SIZE_IN_MB MB")
    //    }
    //    maxSizeInMB * 1024 * 1024
    //}
}
