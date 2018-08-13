package com.asa.spark.rpc.internalimp.env;

import com.asa.spark.rpc.internalimp.conf.SparkConf;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 * env 的配置信息
 */
public class RpcEnvConfig {

    private SparkConf conf;

    private String name;

    private String bindAddress;

    private String advertiseAddress;

    private int port;

    private int numUsableCores;

    private boolean clientMode;

    private SecurityManager securityManager;

    public SparkConf getConf() {

        return conf;
    }

    public void setConf(SparkConf conf) {

        this.conf = conf;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getBindAddress() {

        return bindAddress;
    }

    public void setBindAddress(String bindAddress) {

        this.bindAddress = bindAddress;
    }

    public String getAdvertiseAddress() {

        return advertiseAddress;
    }

    public void setAdvertiseAddress(String advertiseAddress) {

        this.advertiseAddress = advertiseAddress;
    }

    public int getPort() {

        return port;
    }

    public void setPort(int port) {

        this.port = port;
    }

    public int getNumUsableCores() {

        return numUsableCores;
    }

    public void setNumUsableCores(int numUsableCores) {

        this.numUsableCores = numUsableCores;
    }

    public boolean isClientMode() {

        return clientMode;
    }

    public void setClientMode(boolean clientMode) {

        this.clientMode = clientMode;
    }

    public SecurityManager getSecurityManager() {

        return securityManager;
    }

    public void setSecurityManager(SecurityManager securityManager) {

        this.securityManager = securityManager;
    }
}
