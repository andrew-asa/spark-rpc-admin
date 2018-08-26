package com.asa.spark.rpc.internalimp.common.network.server;

import com.asa.spark.rpc.internalimp.common.network.buffer.ManagedBuffer;
import com.asa.spark.rpc.internalimp.common.network.client.TransportClient;
import io.netty.channel.Channel;

/**
 * @author andrew_asa
 * @date 2018/8/26.
 */
public class StreamManagerAdapter implements StreamManager {

    @Override
    public ManagedBuffer getChunk(long streamId, int chunkIndex) {

        return null;
    }

    @Override
    public ManagedBuffer openStream(String streamId) {

        return null;
    }

    @Override
    public void registerChannel(Channel channel, long streamId) {

    }

    @Override
    public void connectionTerminated(Channel channel) {

    }

    @Override
    public void checkAuthorization(TransportClient client, long streamId) {

    }

    @Override
    public long chunksBeingTransferred() {

        return 0;
    }

    @Override
    public void chunkBeingSent(long streamId) {

    }

    @Override
    public void streamBeingSent(String streamId) {

    }

    @Override
    public void chunkSent(long streamId) {

    }

    @Override
    public void streamSent(String streamId) {

    }
}
