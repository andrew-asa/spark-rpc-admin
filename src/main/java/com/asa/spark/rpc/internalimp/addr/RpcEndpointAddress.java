package com.asa.spark.rpc.internalimp.addr;

import com.asa.spark.rpc.expection.SparkException;
import com.asa.spark.rpc.utils.StringFormatUtils;

import java.net.URI;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public class RpcEndpointAddress {

    private String name;

    private RpcAddress rpcAddress;

    public RpcEndpointAddress() {

    }


    public RpcEndpointAddress(String host, int port, String name) {

        this.name = name;
        this.rpcAddress = new RpcAddress(host, port);
    }

    public RpcEndpointAddress(RpcAddress address, String name) {

        this.rpcAddress = address;
        this.name = name;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public RpcAddress getRpcAddress() {

        return rpcAddress;
    }

    public void setRpcAddress(RpcAddress rpcAddress) {

        this.rpcAddress = rpcAddress;
    }

    @Override
    public String toString() {

        if (rpcAddress != null) {
            return StringFormatUtils.format("spark://%s@%s:%s",name,rpcAddress.getHost(),rpcAddress.getPort());
        } else {
            return "spark-client://" + name;
        }
    }

    public static RpcEndpointAddress apply(String host, int port, String name) {

        return new RpcEndpointAddress(host, port, name);
    }

    public static RpcEndpointAddress apply(String sparkUrl) throws Exception {

        try {
            URI uri = new java.net.URI(sparkUrl);
            String host = uri.getHost();
            int port = uri.getPort();
            String name = uri.getUserInfo();
            if (uri.getScheme() != "spark" ||
                    host == null ||
                    port < 0 ||
                    name == null ||
                    (uri.getPath() != null && !uri.getPath().isEmpty()) ||
                    uri.getFragment() != null ||
                    uri.getQuery() != null) {
                throw new SparkException("Invalid Spark URL: " + sparkUrl);
            }
            return new RpcEndpointAddress(host, port, name);
        } catch (Exception e) {
            throw new SparkException("Invalid Spark URL: " + sparkUrl, e);
        }
    }
}
