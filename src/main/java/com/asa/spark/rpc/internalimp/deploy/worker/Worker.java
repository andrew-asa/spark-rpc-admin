package com.asa.spark.rpc.internalimp.deploy.worker;

import com.asa.spark.rpc.expection.SparkUncaughtExceptionHandler;
import com.asa.spark.rpc.internalimp.conf.SparkConf;
import com.asa.spark.rpc.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author andrew_asa
 * @date 2018/8/20.
 */
public class Worker {

    private static Logger LOGGER = LoggerFactory.getLogger(Worker.class);

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(new SparkUncaughtExceptionHandler(false));
        CommonUtils.initDaemon(LOGGER);
        SparkConf conf = new SparkConf();
    }
}
