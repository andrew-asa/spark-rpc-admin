package com.asa.spark.rpc.internalimp.netty.msg.in;

import com.asa.spark.rpc.internalimp.addr.RpcAddress;
import com.asa.spark.rpc.internalimp.addr.RpcEndpointAddress;
import com.asa.spark.rpc.internalimp.common.network.client.TransportClient;
import com.asa.spark.rpc.internalimp.io.in.ByteBufferInputStream;
import com.asa.spark.rpc.internalimp.io.out.ByteBufferOutputStream;
import com.asa.spark.rpc.internalimp.netty.NettyRpcEndpointRef;
import com.asa.spark.rpc.internalimp.netty.NettyRpcEnv;
import com.asa.spark.rpc.serializer.SerializationStream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public class RequestMessage {

    private RpcAddress senderAddress;

    private NettyRpcEndpointRef receiver;

    private Object content;

    public RequestMessage() {

    }

    public RequestMessage(RpcAddress senderAddress, NettyRpcEndpointRef receiver, Object content) {

        this.senderAddress = senderAddress;
        this.receiver = receiver;
        this.content = content;
    }

    public RpcAddress getSenderAddress() {

        return senderAddress;
    }

    public void setSenderAddress(RpcAddress senderAddress) {

        this.senderAddress = senderAddress;
    }

    public NettyRpcEndpointRef getReceiver() {

        return receiver;
    }

    public void setReceiver(NettyRpcEndpointRef receiver) {

        this.receiver = receiver;
    }

    public Object getContent() {

        return content;
    }

    public void setContent(Object content) {

        this.content = content;
    }

    public ByteBuffer serialize(NettyRpcEnv nettyEnv) throws Exception {

        ByteBufferOutputStream bos = new ByteBufferOutputStream();
        DataOutputStream out = new DataOutputStream(bos);
        try {
            writeRpcAddress(out, senderAddress);
            writeRpcAddress(out, receiver.getEndpointAddress().getRpcAddress());
            out.writeUTF(receiver.getName());
            SerializationStream s = nettyEnv.serializeStream(out);
            try {
                s.writeObject(content);
            } finally {
                s.close();
            }
        } finally {
            out.close();
        }
        return bos.toByteBuffer();
    }

    private void writeRpcAddress(DataOutputStream out, RpcAddress rpcAddress) throws Exception {

        if (rpcAddress == null) {
            out.writeBoolean(false);
        } else {
            out.writeBoolean(true);
            out.writeUTF(rpcAddress.getHost());
            out.writeInt(rpcAddress.getPort());
        }
    }

    public RpcAddress readRpcAddress(DataInputStream in) throws IOException {

        boolean hasRpcAddress = in.readBoolean();
        if (hasRpcAddress) {
            return new RpcAddress(in.readUTF(), in.readInt());
        } else {
            return null;
        }
    }

    public RequestMessage apply(NettyRpcEnv nettyEnv, TransportClient client, ByteBuffer bytes) throws IOException {

        ByteBufferInputStream bis = new ByteBufferInputStream(bytes);
        DataInputStream in = new DataInputStream(bis);
        try {
            RpcAddress senderAddress = readRpcAddress(in);
            RpcEndpointAddress endpointAddress = new RpcEndpointAddress(readRpcAddress(in), in.readUTF());
            NettyRpcEndpointRef ref = new NettyRpcEndpointRef(nettyEnv.getConf(), endpointAddress, nettyEnv);
            ref.setClient(client);
            return new RequestMessage(
                    senderAddress,
                    ref,
                    // The remaining bytes in `bytes` are the message content.
                    nettyEnv.deserialize(client, bytes));
        } finally {
            in.close();
        }
    }
}
