package com.asa.spark.rpc.demo.netty.handler.service;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by andrew_asa on 2018/8/5.
 */
public class EchoServiceHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        ctx.write(msg);
        ctx.flush();
    }
}
