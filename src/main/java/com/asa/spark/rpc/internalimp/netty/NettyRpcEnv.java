package com.asa.spark.rpc.internalimp.netty;

import com.asa.spark.rpc.internalimp.conf.SparkConf;
import com.asa.spark.rpc.internalimp.endpoint.RpcEndpointRef;
import com.asa.spark.rpc.internalimp.env.RpcEnv;
import com.asa.spark.rpc.internalimp.env.RpcEnvFileServer;
import com.asa.spark.rpc.internalimp.endpoint.RpcEndpoint;
import com.asa.spark.rpc.serializer.JavaSerializerInstance;
import com.asa.spark.rpc.serializer.SerializationStream;

import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

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
}
