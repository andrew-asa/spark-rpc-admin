package com.asa.spark.rpc.utils;

import com.asa.spark.rpc.expection.SparkRunTimeException;
import com.google.common.collect.ImmutableMap;

import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public class CommonUtils {

    /**
     * 如果是source是以prefix开头则进行去掉
     *
     * @param source
     * @param prefix
     * @return
     */
    public static String stripPrefix(String source, String prefix) {

        if (prefix != null && source.startsWith(prefix)) {
            return source.substring(prefix.length());
        }
        return source;
    }

    /**
     * 如果是source是以suffix结尾则进行去掉
     *
     * @param source
     * @param suffix
     * @return
     */
    public static String stripSuffix(String source, String suffix) {

        if (suffix != null && source.endsWith(suffix)) {
            return source.substring(0, source.length() - suffix.length());
        }
        return source;
    }

    /**
     * 校验
     *
     * @param req
     * @param errorMsg
     */
    public static void require(boolean req, String errorMsg) {

        if (!req) {
            throw new SparkRunTimeException(errorMsg);
        }
    }

    public static boolean allNotNull(Object... args) {

        for (Object arg : args) {
            if (arg == null) {
                return false;
            }
        }
        return true;
    }
}
