package com.asa.spark.rpc.utils;

import com.asa.spark.rpc.constant.CommonConstant;
import com.asa.spark.rpc.expection.SparkException;
import com.asa.spark.rpc.internalimp.addr.RpcAddress;

import java.net.URI;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public class URLUtils {

    public static RpcAddress extractHostPortFromSparkUrl(String sparkUrl) throws Exception {

        try {
            URI uri = new URI(sparkUrl);
            String host = uri.getHost();
            int port = uri.getPort();
            if (!CommonConstant.URLPERRFIX.equals(uri.getScheme()) ||
                    host == null ||
                    port < 0 ||
                    (uri.getPath() != null && !uri.getPath().isEmpty()) ||
                    uri.getFragment() != null ||
                    uri.getQuery() != null ||
                    uri.getUserInfo() != null) {
                throw new SparkException("Invalid master URL: " + sparkUrl);
            }
            return new RpcAddress(host, port);
        } catch (Exception e) {
            throw new SparkException("Invalid master URL: " + sparkUrl, e);
        }
    }
}
