package com.asa.spark.rpc.serializer;

import java.io.Closeable;
import java.util.Iterator;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public abstract class SerializationStream implements Closeable {

    public abstract void writeObject(Object value);

    public abstract void writeKey(String key);

    public abstract void writeValue(Object value);

    public abstract void flush();

    SerializationStream writeAll(Iterator<Object> iter) {

        while (iter.hasNext()) {
            writeObject(iter.next());
        }
        return this;
    }
}
