package com.asa.spark.rpc.internalimp.common.network.data;

import java.util.Map;
import java.util.Objects;

/**
 * @author andrew_asa
 * @date 2018/8/12.
 */
public class Pair<K, V> implements Map.Entry<K, V> {

    private K key;

    private V value;

    public Pair(K key, V value) {

        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {

        return key;
    }

    @Override
    public V getValue() {

        return value;
    }

    @Override
    public V setValue(V value) {

        V o = this.value;
        return o;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (!(o instanceof Pair)) {
            return false;
        }
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(key, pair.key) &&
                Objects.equals(value, pair.value);
    }

    @Override
    public int hashCode() {

        return Objects.hash(key, value);
    }
}
