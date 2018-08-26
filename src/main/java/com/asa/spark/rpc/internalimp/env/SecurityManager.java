package com.asa.spark.rpc.internalimp.env;

import com.asa.spark.rpc.internalimp.conf.SparkConf;

/**
 * @author andrew_asa
 * @date 2018/8/12.
 */
public class SecurityManager {

    private SparkConf conf;

    public SecurityManager(SparkConf conf) {

        this.conf = conf;
    }
}
