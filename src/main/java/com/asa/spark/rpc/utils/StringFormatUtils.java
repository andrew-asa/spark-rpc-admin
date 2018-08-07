package com.asa.spark.rpc.utils;

/**
 * @author andrew_asa
 * @date 2018/8/7.
 */
public class StringFormatUtils {

    /**
     * 字符串格式化
     *
     * @param format
     * @param args
     * @return
     */
    public static String format(String format, Object... args) {

        return String.format(format, args);
    }
}
