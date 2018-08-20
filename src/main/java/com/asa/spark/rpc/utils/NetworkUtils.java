package com.asa.spark.rpc.utils;

import com.asa.spark.rpc.internalimp.conf.SparkConf;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author andrew_asa
 * @date 2018/8/13.
 * 网络相关工具类
 */
public class NetworkUtils {

    private static InetAddress localIpAddress = findLocalInetAddress();

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


    /**
     * 获取本机地址
     *
     * @return
     * @throws Exception
     */
    public static InetAddress findLocalInetAddress() {

        try {
            String defaultIpOverride = System.getenv("SPARK_LOCAL_IP");
            if (defaultIpOverride != null) {
                return InetAddress.getByName(defaultIpOverride);
            } else {
                InetAddress address = InetAddress.getLocalHost();
                if (address.isLoopbackAddress()) {
                    // Address resolves to something like 127.0.1.1, which happens on Debian; try to find
                    // a better address using the local network interfaces
                    // getNetworkInterfaces returns ifs in reverse order compared to ifconfig output order
                    // on unix-like system. On windows, it returns in index order.
                    // It's more proper to pick ip address following system output order.
                    List<NetworkInterface> activeNetworkIFs = CommonUtils.enumerationToList(NetworkInterface.getNetworkInterfaces());
                    List<NetworkInterface> reOrderedNetworkIFs = activeNetworkIFs;
                    if (!SystemUtils.IS_OS_WINDOWS) {
                        Collections.reverse(activeNetworkIFs);
                    }
                    for (NetworkInterface ni : reOrderedNetworkIFs) {
                        List<InetAddress> addresses = CommonUtils.enumerationToList(ni.getInetAddresses());
                        addresses = addresses.stream().filter(addr -> addr.isLinkLocalAddress() || addr.isLoopbackAddress()).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(addresses)) {
                            List<InetAddress> ipv4 = addresses.stream().filter(addr -> addr instanceof Inet4Address).collect(Collectors.toList());
                            if (CollectionUtils.isNotEmpty(ipv4)) {
                                address = ipv4.get(0);
                            } else {
                                address = addresses.get(0);
                            }
                            return InetAddress.getByAddress(address.getAddress());
                        }
                    }
                }
                return address;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static String getLocalHostName() {

        return localIpAddress.getHostName();
    }
}
