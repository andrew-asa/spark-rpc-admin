package com.asa.spark.rpc.internalimp.netty;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.ReadableByteChannel;

/**
 * @author andrew_asa
 * @date 2018/8/12.
 */
public class FileDownloadChannel implements ReadableByteChannel {

    private Pipe.SourceChannel source;

    public FileDownloadChannel(Pipe.SourceChannel source) {

        this.source = source;
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {

        return source.read(dst);
    }

    @Override
    public boolean isOpen() {

        return source.isOpen();
    }

    @Override
    public void close() throws IOException {

        source.close();
    }
}
