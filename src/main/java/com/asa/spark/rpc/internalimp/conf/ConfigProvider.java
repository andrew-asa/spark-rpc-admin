package com.asa.spark.rpc.internalimp.conf;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */

import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Provides a mechanism for constructing a {@link TransportConf} using some sort of configuration.
 */
public abstract class ConfigProvider {
    /** Obtains the value of the given config, throws NoSuchElementException if it doesn't exist. */
    public abstract String get(String name);

    /** Returns all the config values in the provider. */
    public abstract Iterable<Map.Entry<String, String>> getAll();

    public String get(String name, String defaultValue) {
        try {
            return get(name);
        } catch (NoSuchElementException e) {
            return defaultValue;
        }
    }

    public int getInt(String name, int defaultValue) {
        return Integer.parseInt(get(name, Integer.toString(defaultValue)));
    }

    public long getLong(String name, long defaultValue) {
        return Long.parseLong(get(name, Long.toString(defaultValue)));
    }

    public double getDouble(String name, double defaultValue) {
        return Double.parseDouble(get(name, Double.toString(defaultValue)));
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        return Boolean.parseBoolean(get(name, Boolean.toString(defaultValue)));
    }

}
