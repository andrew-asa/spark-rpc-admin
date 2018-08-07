package com.asa.spark.rpc.internalimp.netty;

import com.asa.spark.rpc.internalimp.addr.RpcEndpointAddress;
import com.asa.spark.rpc.internalimp.conf.SparkConf;
import com.asa.spark.rpc.internalimp.endpoint.RpcEndpointRef;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public class NettyRpcEndpointRef extends RpcEndpointRef {

    private SparkConf conf;

    private RpcEndpointAddress endpointAddress;

    private NettyRpcEnv rpcEnv;

    public NettyRpcEndpointRef(SparkConf conf, RpcEndpointAddress endpointAddress, NettyRpcEnv rpcEnv) {

        super();
        this.conf = conf;
        this.endpointAddress = endpointAddress;
        this.rpcEnv = rpcEnv;
    }

    @Override
    public void send(Object message) {

    }

    public RpcEndpointAddress getEndpointAddress() {

        return endpointAddress;
    }

    public String getAddr() {

        return endpointAddress.getRpcAddress().toString();

    }

    @Override
    public String getName() {

        return endpointAddress.getName();
    }
}
