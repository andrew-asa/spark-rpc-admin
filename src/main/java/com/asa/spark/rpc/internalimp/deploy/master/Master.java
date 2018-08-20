package com.asa.spark.rpc.internalimp.deploy.master;

import com.asa.spark.rpc.internalimp.conf.SparkConf;

/**
 * @author andrew_asa
 * @date 2018/8/20.
 */
public class Master {

    public Master(String[] args) {

        SparkConf conf = new SparkConf();
        MasterArguments masterArguments = new MasterArguments(conf, args);
        startRpcEnvAndEndpoint(masterArguments.getHost(), masterArguments.getPort(), masterArguments.getWebUiPort(), conf);
    }

    public static void main(String[] args) {

        new Master(args);
    }

    private void startRpcEnvAndEndpoint(String host, int port, int webUiPort, SparkConf conf) {

    }
}
