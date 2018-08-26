package com.asa.spark.rpc.internalimp.netty;

import com.asa.spark.rpc.internalimp.addr.RpcAddress;
import com.asa.spark.rpc.internalimp.common.network.client.RpcResponseCallback;
import com.asa.spark.rpc.internalimp.common.network.client.TransportClient;
import com.asa.spark.rpc.internalimp.common.network.server.RpcHandler;
import com.asa.spark.rpc.internalimp.common.network.server.StreamManager;
import com.asa.spark.rpc.internalimp.netty.msg.in.RemoteProcessConnected;
import com.asa.spark.rpc.internalimp.netty.msg.in.RequestMessage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author andrew_asa
 * @date 2018/8/9.
 */
public class NettyRpcHandler extends RpcHandler {

    private Dispatcher dispatcher;

    private NettyRpcEnv nettyEnv;

    private StreamManager streamManager;

    private ConcurrentHashMap<RpcAddress, RpcAddress> remoteAddresses = new ConcurrentHashMap<RpcAddress, RpcAddress>();


    public NettyRpcHandler(Dispatcher dispatcher, NettyRpcEnv nettyEnv, StreamManager streamManager) {

        this.dispatcher = dispatcher;
        this.nettyEnv = nettyEnv;
        this.streamManager = streamManager;
    }

    public Dispatcher getDispatcher() {

        return dispatcher;
    }

    public void setDispatcher(Dispatcher dispatcher) {

        this.dispatcher = dispatcher;
    }

    public NettyRpcEnv getNettyEnv() {

        return nettyEnv;
    }

    public void setNettyEnv(NettyRpcEnv nettyEnv) {

        this.nettyEnv = nettyEnv;
    }

    @Override
    public void receive(TransportClient client, ByteBuffer message, RpcResponseCallback callback) {

    }

    private RequestMessage internalReceive(TransportClient client, ByteBuffer message) throws IOException {

        InetSocketAddress addr = (InetSocketAddress) client.getChannel().remoteAddress();
        assert (addr != null);
        RpcAddress clientAddr = new RpcAddress(addr.getHostString(), addr.getPort());
        RequestMessage requestMessage = new RequestMessage().apply(nettyEnv, client, message);
        if (requestMessage.getSenderAddress() == null) {
            // Create a new message with the socket address of the client as the sender.
            return new RequestMessage(clientAddr, requestMessage.getReceiver(), requestMessage.getContent());
        } else {
            // The remote RpcEnv listens to some port, we should also fire a RemoteProcessConnected for
            // the listening address
            RpcAddress remoteEnvAddress = requestMessage.getSenderAddress();
            if (remoteAddresses.putIfAbsent(clientAddr, remoteEnvAddress) == null) {
                dispatcher.postToAll(new RemoteProcessConnected(remoteEnvAddress));
            }
            return requestMessage;
        }
    }

    @Override
    public StreamManager getStreamManager() {

        return streamManager;
    }

    public void setStreamManager(StreamManager streamManager) {

        this.streamManager = streamManager;
    }
}
