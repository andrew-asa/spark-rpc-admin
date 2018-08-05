package com.asa.spark.rpc.serializer;

import com.asa.spark.rpc.internalimp.io.out.ByteBufferOutputStream;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public class JavaSerializerInstance implements SerializerInstance {

    private int counterReset;

    private boolean extraDebugInfo;

    private ClassLoader classLoader;

    public JavaSerializerInstance(int counterReset, boolean extraDebugInfo, ClassLoader classLoader) {

        this.counterReset = counterReset;
        this.extraDebugInfo = extraDebugInfo;
        this.classLoader = classLoader;
    }

    public int getCounterReset() {

        return counterReset;
    }

    public void setCounterReset(int counterReset) {

        this.counterReset = counterReset;
    }

    public boolean isExtraDebugInfo() {

        return extraDebugInfo;
    }

    public void setExtraDebugInfo(boolean extraDebugInfo) {

        this.extraDebugInfo = extraDebugInfo;
    }

    public ClassLoader getClassLoader() {

        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {

        this.classLoader = classLoader;
    }

    @Override
    public ByteBuffer serialize(Object value) throws Exception {

        ByteBufferOutputStream bos = new ByteBufferOutputStream();
        SerializationStream out = serializeStream(bos);
        out.writeObject(value);
        out.close();
        return bos.toByteBuffer();
    }

    @Override
    public <T> T deserialize(ByteBuffer bytes) {

        return null;
    }

    @Override
    public <T> T deserialize(ByteBuffer bytes, ClassLoader loader) {

        return null;
    }

    @Override
    public SerializationStream serializeStream(OutputStream s) {

        return new JavaSerializationStream(s, counterReset, extraDebugInfo);
    }

    @Override
    public DeserializationStream deserializeStream(InputStream s) {

        return null;
    }
}
