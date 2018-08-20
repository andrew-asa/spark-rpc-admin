package com.asa.spark.rpc.internalimp.deploy.master;

import com.asa.spark.rpc.internalimp.conf.SparkConf;
import com.asa.spark.rpc.utils.NetworkUtils;

import java.util.List;

/**
 * @author andrew_asa
 * @date 2018/8/20.
 */
public class MasterArguments {

    private String host = NetworkUtils.getLocalHostName();

    private int port = 7077;

    private int webUiPort = 8080;

    private String propertiesFile;

    public MasterArguments(SparkConf conf, String[] args) {

    }

    private void parse(SparkConf conf, String[] args) {

    }

    public String getHost() {

        return host;
    }

    public void setHost(String host) {

        this.host = host;
    }

    public int getPort() {

        return port;
    }

    public void setPort(int port) {

        this.port = port;
    }

    public int getWebUiPort() {

        return webUiPort;
    }

    public void setWebUiPort(int webUiPort) {

        this.webUiPort = webUiPort;
    }

    public String getPropertiesFile() {

        return propertiesFile;
    }

    public void setPropertiesFile(String propertiesFile) {

        this.propertiesFile = propertiesFile;
    }
}
