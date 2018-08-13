package com.asa.spark.rpc.utils;

import com.asa.spark.rpc.internalimp.conf.SparkConf;
import org.apache.commons.lang3.StringUtils;

/**
 * @author andrew_asa
 * @date 2018/8/13.
 * 网络相关工具类
 */
public class NetworkUtils {

    public static void startServiceOnPort(int startPort, int startService, SparkConf conf, String serviceName) {

        CommonUtils.require(startPort == 0 || (1024 <= startPort && startPort < 65536),

                            "startPort should be between 1024 and 65535 (inclusive), or 0 for a random free port.");
        String serviceString;
        if (StringUtils.isEmpty(serviceName)) {
            serviceString = StringUtils.EMPTY;
        } else {
            serviceString = serviceName;
        }
        int maxRetries = portMaxRetries(conf);
        for (int i = 0; i < maxRetries; i++) {
            int tryPort = startPort == 0 ? startPort : userPort(startPort, i);
            //try {
            //
            //}
        }
    }

    /**
     * Maximum number of retries when binding to a port before giving up.
     */
    public static int portMaxRetries(SparkConf conf) {

        if (conf.contains("spark.testing")) {
            // Set a higher number of retries for tests...
            return conf.getInt("spark.port.maxRetries", 100);
        } else {
            return conf.getInt("spark.port.maxRetries", 16);
        }
    }

    public static int userPort(int base, int offset) {

        return (base + offset - 1024) % (65536 - 1024) + 1024;
    }

}
