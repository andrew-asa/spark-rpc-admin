package com.asa.spark.rpc.internalimp.io.out;

import com.asa.spark.rpc.utils.CommonUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public class ByteBufferOutputStream extends ByteArrayOutputStream {

    private int count;

    private boolean closed = false;

    public ByteBufferOutputStream() {

        this(32);
    }

    public ByteBufferOutputStream(int capacity) {

        super(capacity);
    }

    @Override
    public void write(int b) {

        CommonUtils.require(!closed, "cannot write to a closed ByteBufferOutputStream");
        super.write(b);
    }

    @Override
    public void write(byte b[], int off, int len) {

        CommonUtils.require(!closed, "cannot write to a closed ByteBufferOutputStream");
        super.write(b, off, len);
    }

    @Override
    public void reset() {

        CommonUtils.require(!closed, "cannot reset a closed ByteBufferOutputStream");
        super.reset();
    }

    @Override
    public void close() throws IOException {

        if (!closed) {
            super.close();
            closed = true;
        }
    }

    public ByteBuffer toByteBuffer() {

        CommonUtils.require(closed, "can only call toByteBuffer() after ByteBufferOutputStream has been closed");
        return ByteBuffer.wrap(buf, 0, count);
    }
}
