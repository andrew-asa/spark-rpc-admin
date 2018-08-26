package com.asa.spark.rpc.internalimp.netty;

import com.asa.spark.rpc.internalimp.common.network.buffer.FileSegmentManagedBuffer;
import com.asa.spark.rpc.internalimp.common.network.buffer.ManagedBuffer;
import com.asa.spark.rpc.internalimp.common.network.client.TransportClient;
import com.asa.spark.rpc.internalimp.common.network.server.StreamManager;
import com.asa.spark.rpc.internalimp.env.RpcEnvFileServer;
import com.asa.spark.rpc.utils.CommonUtils;
import com.asa.spark.rpc.utils.StringFormatUtils;
import io.netty.channel.Channel;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public class NettyStreamManager extends RpcEnvFileServer implements StreamManager {

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

    @Override
    public ManagedBuffer openStream(String streamId) {

        File file = null;
        String tstr = CommonUtils.stripPrefix(streamId, "/");
        String[] info = tstr.split("/");
        CommonUtils.require(info.length == 2, "invalid streamId %s", streamId);
        String ftype = info[0];
        String fname = info[1];
        if (ftype.matches("files")) {
            file = files.get(fname);
        } else if (ftype.matches("jars")) {
            file = jars.get(fname);
        } else {
            File dir = dirs.get(ftype);
            CommonUtils.require(dir != null, "Invalid stream URI: %s not found.", ftype);
        }
        if (file != null && file.isFile()) {
            return new FileSegmentManagedBuffer(rpcEnv.getTransportConf(), file, 0, file.length());
        }
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

    @Override
    public String addFile(File file) {

        File existingPath = files.putIfAbsent(file.getName(), file);
        CommonUtils.require(existingPath == null || existingPath == file,
                            "File %s was already registered with a different path (old path = %s, new path = %s"
                , file.getName(), existingPath.getName(), file.getName());
        String sparUrl = rpcEnv.getAddress().toSparkURL();
        String encodeFileName = CommonUtils.encodeFileNameToURIRawPath(file.getName());
        return StringFormatUtils.format("%s/files/%s", sparUrl, encodeFileName);
    }

    @Override
    public String addJar(File file) {

        File existingPath = jars.putIfAbsent(file.getName(), file);
        CommonUtils.require(existingPath == null || existingPath == file,
                            "File %s was already registered with a different path (old path = %s, new path = %s"
                , file.getName(), existingPath.getName(), file.getName());
        String sparUrl = rpcEnv.getAddress().toSparkURL();
        String encodeFileName = CommonUtils.encodeFileNameToURIRawPath(file.getName());
        return StringFormatUtils.format("%s/jars/%s", sparUrl, encodeFileName);
    }

    @Override
    public String addDirectory(String baseUri, File path) {

        String fixedBaseUri = validateDirectoryUri(baseUri);
        File file = dirs.putIfAbsent(CommonUtils.stripPrefix(fixedBaseUri, "/"), path);
        CommonUtils.require(file == null,
                            "URI '%s' already registered.", fixedBaseUri);
        String sparUrl = rpcEnv.getAddress().toSparkURL();
        return sparUrl + fixedBaseUri;
    }
}
