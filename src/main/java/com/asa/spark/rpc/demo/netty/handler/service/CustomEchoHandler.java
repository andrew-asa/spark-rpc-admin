package com.asa.spark.rpc.demo.netty.handler.service;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 * 自己定义返回的信息处理器
 */
public class CustomEchoHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        try {
            ByteBuf in = (ByteBuf) msg;
            String say = in.toString(io.netty.util.CharsetUtil.US_ASCII);
            String rep = "";
            boolean close = false;
            if (say.indexOf("hello") != -1) {
                rep = "word";
            } else {
                rep = "unknown what you say\n";
                close = true;
            }
            byte[] bytes = rep.getBytes();
            ByteBuf byteBuf = ctx.alloc().buffer(bytes.length);
            byteBuf.writeBytes(bytes);
            ChannelFuture future = ctx.writeAndFlush(byteBuf);
            if (close) {
                future.addListener(new ChannelFutureListener() {

                    @Override
                    public void operationComplete(ChannelFuture f) {

                        assert future == f;
                        ctx.close();
                    }
                });
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
