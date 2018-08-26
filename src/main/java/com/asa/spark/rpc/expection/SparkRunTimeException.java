package com.asa.spark.rpc.expection;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public class SparkRunTimeException extends RuntimeException {

    public SparkRunTimeException() {

    }

    public SparkRunTimeException(String msg) {

        super(msg);
    }

    public SparkRunTimeException(String msgPatter, Object... args) {

        super(String.format(msgPatter, args));
    }
}
