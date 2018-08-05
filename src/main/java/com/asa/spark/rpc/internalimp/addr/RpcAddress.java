package com.asa.spark.rpc.internalimp.addr;

import com.asa.spark.rpc.utils.URLUtils;

import java.net.URI;
import java.util.Objects;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 * rpc 地址
 */
public class RpcAddress {

    private String hostPort;

    private int port;

    private String host;

    private String toSparkURL;

    public RpcAddress(String host, int port) {

        this.port = port;
        this.host = host;
        this.host = host + ":" + port;
    }

    public String getHostPort() {

        return hostPort;
    }

    public void setHostPort(String hostPort) {

        this.hostPort = hostPort;
    }

    public int getPort() {

        return port;
    }

    public void setPort(int port) {

        this.port = port;
    }

    public String getHost() {

        return host;
    }

    public void setHost(String host) {

        this.host = host;
    }

    public String toSparkURL() {

        return "spark://" + hostPort;
    }

    public static RpcAddress fromURIString(String uri) throws Exception {

        URI uriObj = new URI(uri);
        RpcAddress ret = new RpcAddress(uriObj.getHost(), uriObj.getPort());
        return ret;
    }

    public static RpcAddress fromSparkURL(String sparkUrl) throws Exception {

        return URLUtils.extractHostPortFromSparkUrl(sparkUrl);
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (!(o instanceof RpcAddress)) {
            return false;
        }
        RpcAddress that = (RpcAddress) o;
        return port == that.port &&
                Objects.equals(host, that.host);
    }

    @Override
    public int hashCode() {

        return Objects.hash(port, host);
    }
}
