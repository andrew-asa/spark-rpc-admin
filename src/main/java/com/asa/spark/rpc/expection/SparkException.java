package com.asa.spark.rpc.expection;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public class SparkException extends Exception {

    public SparkException(String msg) {

        super(msg);
    }

    public SparkException(String msg, Throwable cause) {

        super(msg, cause);
    }
}
