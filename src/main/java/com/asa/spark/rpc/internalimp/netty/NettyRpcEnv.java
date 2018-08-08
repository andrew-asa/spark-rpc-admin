package com.asa.spark.rpc.internalimp.netty;

import com.asa.spark.rpc.internalimp.common.network.context.TransportContext;
import com.asa.spark.rpc.internalimp.common.network.server.TransportServer;
import com.asa.spark.rpc.internalimp.conf.SparkConf;
import com.asa.spark.rpc.internalimp.endpoint.RpcEndpointRef;
import com.asa.spark.rpc.internalimp.env.RpcEnv;
import com.asa.spark.rpc.internalimp.env.RpcEnvFileServer;
import com.asa.spark.rpc.internalimp.endpoint.RpcEndpoint;
import com.asa.spark.rpc.serializer.JavaSerializerInstance;
import com.asa.spark.rpc.serializer.SerializationStream;
import com.asa.spark.rpc.utils.ThreadUtils;

import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 * 基于netty 实现的rpc环境
 */
public class NettyRpcEnv extends RpcEnv {

    private SparkConf conf;

    private JavaSerializerInstance javaSerializerInstance;

    private String host;

    private int numUsableCores;

    private Dispatcher dispatcher;

    private ScheduledExecutorService timeoutScheduler = ThreadUtils.newDaemonSingleThreadScheduledExecutor("netty-rpc-env-timeout");

    // Because TransportClientFactory.createClient is blocking, we need to run it in this thread pool
    // to implement non-blocking send/ask.
    // TODO: a non-blocking TransportClientFactory.createClient in future
    private ThreadPoolExecutor clientConnectionExecutor = ThreadUtils.newDaemonCachedThreadPool(
            "netty-rpc-connection",
            conf.getInt("spark.rpc.connect.threads", 64));

    private TransportServer server;



    private TransportContext transportContext = new TransportContext(transportConf,
                                                        new NettyRpcHandler(dispatcher, this, streamManager));

    private val clientFactory = transportContext.createClientFactory(createClientBootstraps())


    public NettyRpcEnv(SparkConf conf, JavaSerializerInstance javaSerializerInstance, String host, int numUsableCores) {

        this.conf = conf;
        this.javaSerializerInstance = javaSerializerInstance;
        this.host = host;
        this.numUsableCores = numUsableCores;
        this.dispatcher = new Dispatcher(this, numUsableCores);
    }

    @Override
    public RpcEndpointRef endpointRef(RpcEndpoint endpoint) {

        return null;
    }

    @Override
    public RpcEndpointRef setupEndpoint(String name, RpcEndpoint endpoint) {

        return null;
    }

    @Override
    public RpcEnvFileServer fileServer() {

        return null;
    }

    @Override
    public ReadableByteChannel openChannel(String uri) {

        return null;
    }

    public SparkConf getConf() {

        return conf;
    }

    public ByteBuffer serialize(Object content) throws Exception {

        return javaSerializerInstance.serialize(content);
    }

    /**
     * Returns [[SerializationStream]] that forwards the serialized bytes to `out`.
     */
    public SerializationStream serializeStream(OutputStream out) {

        return javaSerializerInstance.serializeStream(out);
    }

    public ThreadPoolExecutor getClientConnectionExecutor() {

        return clientConnectionExecutor;
    }

    public void setClientConnectionExecutor(ThreadPoolExecutor clientConnectionExecutor) {

        this.clientConnectionExecutor = clientConnectionExecutor;
    }
}
