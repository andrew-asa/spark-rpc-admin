package com.asa.spark.rpc.serializer;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public class JavaSerializationStream extends SerializationStream {

    private OutputStream out;

    private int counterReset;

    private boolean extraDebugInfo;

    public JavaSerializationStream(OutputStream out, int counterReset, boolean extraDebugInfo) {

        this.out = out;
        this.counterReset = counterReset;
        this.extraDebugInfo = extraDebugInfo;
    }

    @Override
    public void writeObject(Object value) {

    }

    @Override
    public void writeKey(String key) {

    }

    @Override
    public void writeValue(Object value) {

    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws IOException {

    }
}
