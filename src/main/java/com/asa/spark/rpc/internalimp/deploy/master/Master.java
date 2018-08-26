package com.asa.spark.rpc.internalimp.deploy.master;

import com.asa.spark.rpc.internalimp.addr.RpcAddress;
import com.asa.spark.rpc.internalimp.conf.SparkConf;
import com.asa.spark.rpc.internalimp.endpoint.ThreadSafeRpcEndpoint;
import com.asa.spark.rpc.internalimp.env.RpcEnv;
import com.asa.spark.rpc.internalimp.env.SecurityManager;

/**
 * @author andrew_asa
 * @date 2018/8/20.
 */
public class Master extends ThreadSafeRpcEndpoint {

    public static final String SYSTEM_NAME = "SYSTEM_NAME";

    public static final String ENDPOINT_NAME = "Master";

    private RpcAddress rpcAddress;

    private int webUiPort;

    private SparkConf conf;

    private SecurityManager securityMgr;

    public Master(RpcAddress rpcAddress, int webUiPort, SecurityManager securityManager, SparkConf conf) {

        this.rpcAddress = rpcAddress;
        this.webUiPort = webUiPort;
        this.conf = conf;
        this.securityMgr = securityMgr;
    }

    public Master(String[] args) {

        conf = new SparkConf();
        securityMgr = new SecurityManager(conf);
        MasterArguments masterArguments = new MasterArguments(conf, args);
        startRpcEnvAndEndpoint(masterArguments.getHost(), masterArguments.getPort(), masterArguments.getWebUiPort(), conf);
    }

    public static void main(String[] args) {

        new Master(args);
    }

    private void startRpcEnvAndEndpoint(String host, int port, int webUiPort, SparkConf conf) {

        RpcEnv rpcEnv = RpcEnv.create(SYSTEM_NAME, host, port, conf, securityMgr);
        rpcEnv.setupEndpoint(ENDPOINT_NAME, new Master(rpcEnv.getAddress(), webUiPort, securityMgr, conf));
    }
}
