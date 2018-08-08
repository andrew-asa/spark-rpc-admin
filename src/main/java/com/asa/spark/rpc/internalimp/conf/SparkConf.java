package com.asa.spark.rpc.internalimp.conf;

import com.asa.spark.rpc.utils.JavaUtils;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public class SparkConf {

    private static Map<String, String> settings = new ConcurrentHashMap<String, String>();

    private static Map<String, String> configsWithAlternatives = new ConcurrentHashMap<>();

    private static SparkConf INSTANS = new SparkConf();

    private boolean loadDefaults;

    private SparkConf() {

        this(true);
    }

    private SparkConf(boolean loadDefaults) {

        this.loadDefaults = loadDefaults;
    }

    public static SparkConf getInstants() {

        return INSTANS;
    }

    /**
     * Get a parameter; throws a NoSuchElementException if it's not set
     */
    public String get(String key) {

        String value = getOption(key);
        if (value == null) {
            throw new NoSuchElementException(key);
        }
        return value;
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

        return JavaUtils.timeStringAsMs(getOption(key, defaultValue));
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

    /**
     * Set a configuration variable.
     */
    public SparkConf set(String key, String value) {

        return set(key, value, false);
    }

    private SparkConf set(String key, String value, boolean silent) {

        if (key == null) {
            throw new NullPointerException("null key");
        }
        if (value == null) {
            throw new NullPointerException("null value for " + key);
        }
        if (!silent) {
            //logDeprecationWarning(key);
        }
        settings.put(key, value);
        return this;
    }

    /**
     * Set a parameter if it isn't already configured
     */
    public SparkConf setIfMissing(String key, String value) {

        if (settings.putIfAbsent(key, value) == null) {
            //logDeprecationWarning(key);
        }
        return this;
    }

    /**
     * Get all parameters as a list of pairs
     */
    public List<Map<String, String>> getAll() {

        List<Map<String, String>> ret = new ArrayList<Map<String, String>>();
        settings.entrySet().forEach(new Consumer<Map.Entry<String, String>>() {

            @Override
            public void accept(Map.Entry<String, String> stringStringEntry) {

                Map<String, String> item = new HashMap<String, String>();
                item.put(stringStringEntry.getKey(), stringStringEntry.getValue());
                ret.add(item);
            }
        });
        return ret;
    }

    /**
     * Copy this object
     */
    @Override
    public SparkConf clone() {

        SparkConf cloned = new SparkConf(false);
        settings.entrySet().forEach(new Consumer<Map.Entry<String, String>>() {

            @Override
            public void accept(Map.Entry<String, String> stringStringEntry) {

                cloned.set(stringStringEntry.getKey(), stringStringEntry.getValue());
            }
        });
        return cloned;
    }
}
