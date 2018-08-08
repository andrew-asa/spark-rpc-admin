package com.asa.spark.rpc.internalimp.netty;

import com.asa.spark.rpc.internalimp.common.network.buffer.ManagedBuffer;
import com.asa.spark.rpc.internalimp.common.network.server.StreamManager;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public class NettyStreamManager extends StreamManager {

    private NettyRpcEnv rpcEnv;

    private ConcurrentHashMap<String, File> files = new ConcurrentHashMap<String, File>();

    private ConcurrentHashMap<String, File> jars = new ConcurrentHashMap<String, File>();

    private ConcurrentHashMap<String, File> dirs = new ConcurrentHashMap<String, File>();

    public NettyStreamManager(NettyRpcEnv rpcEnv) {

        this.rpcEnv = rpcEnv;
    }

    @Override
    public ManagedBuffer getChunk(long streamId, int chunkIndex) {

        throw new UnsupportedOperationException();
    }
}
