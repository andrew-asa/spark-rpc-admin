package com.asa.spark.rpc.internalimp.netty;

import com.asa.spark.rpc.internalimp.common.network.client.StreamCallback;
import com.asa.spark.rpc.internalimp.common.network.client.TransportClient;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

/**
 * @author andrew_asa
 * @date 2018/8/12.
 */
public class FileDownloadCallback implements StreamCallback{

    private WritableByteChannel sink;

    private FileDownloadChannel source;

    private TransportClient client;

    public FileDownloadCallback(WritableByteChannel sink, FileDownloadChannel source, TransportClient client) {

        this.sink = sink;
        this.source = source;
        this.client = client;
    }

    @Override
    public void onData(String streamId, ByteBuffer buf) throws IOException {
        while (buf.remaining() > 0) {
            sink.write(buf);
        }
    }

    @Override
    public void onComplete(String streamId) throws IOException {
        sink.close();
    }

    @Override
    public void onFailure(String streamId, Throwable cause) throws IOException {
        //source.(cause)
        sink.close();
    }
}

