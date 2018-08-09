package com.asa.spark.rpc.internalimp.io.in;

import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * @author andrew_asa
 * @date 2018/8/9.
 */
public class ByteBufferInputStream extends InputStream {

    private ByteBuffer buffer;

    public ByteBufferInputStream(ByteBuffer buffer) {

        this.buffer = buffer;
    }

    @Override
    public int read() {

        if (buffer == null || buffer.remaining() == 0) {
            cleanUp();
            return -1;
        } else {
            return buffer.get() & 0xFF;
        }
    }

    @Override
    public int read(byte[] dest) {

        return read(dest, 0, dest.length);
    }

    @Override
    public int read(byte[] dest, int offset, int length) {

        if (buffer == null || buffer.remaining() == 0) {
            cleanUp();
            return -1;
        } else {
            int amountToGet = Math.min(buffer.remaining(), length);
            buffer.get(dest, offset, amountToGet);
            return amountToGet;
        }
    }


    public long skip(Long bytes) {

        if (buffer != null) {
            int amountToSkip = (int) Math.min(bytes, buffer.remaining());
            buffer.position(buffer.position() + amountToSkip);
            if (buffer.remaining() == 0) {
                cleanUp();
            }
            return amountToSkip;
        } else {
            return 0L;
        }
    }

    /**
     * Clean up the buffer, and potentially dispose of it using StorageUtils.dispose().
     */
    private void cleanUp() {

        if (buffer != null) {
            buffer = null;
        }
    }
}
