package com.asa.spark.rpc.internalimp.conf;

import com.asa.spark.rpc.utils.CommonUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public class SparkConf {

    private static Map<String, String> settings = new ConcurrentHashMap<String, String>();

    private static Map<String, String> configsWithAlternatives = new ConcurrentHashMap<>();

    private static SparkConf INSTANS = new SparkConf();

    private SparkConf() {

    }

    public static SparkConf getInstants() {

        return INSTANS;
    }


    /**
     * Get a parameter as an Option
     */
    public static String getOption(String key) {

        return getOption(key, null);
    }

    /**
     * Get a parameter as an Option
     */
    public static String getOption(String key, String defaultValue) {

        if (settings.containsKey(key)) {
            return settings.get(key);
        }
        if (configsWithAlternatives.containsKey(key)) {
            return configsWithAlternatives.get(key);
        }
        return null;
    }

    public int getInt(String key, int defaultValue) {

        String valueStr = getOption(key);
        if (valueStr != null) {
            return Integer.parseInt(valueStr);
        }
        return defaultValue;
    }

    public long getLong(String key, long defaultValue) {

        String valueStr = getOption(key);
        if (valueStr != null) {
            return Long.parseLong(valueStr);
        }
        return defaultValue;
    }

    public long getTimeAsMs(String key, String defaultValue) {

        return CommonUtils.timeStringAsMs(getOption(key, defaultValue));
    }

    static {
        addAlternativesConf(new String[][]{

        });
    }

    public static void addAlternativesConf(String[][] confs) {

        for (int i = 0; i < confs.length; i++) {
            String[] conf = confs[i];
            configsWithAlternatives.put(conf[0], conf[1]);
        }
    }
}
