package com.asa.spark.rpc.demo.netty.service;

import com.asa.spark.rpc.demo.netty.handler.service.EchoServiceHandler;
import io.netty.channel.ChannelHandler;

/**
 * Created by andrew_asa on 2018/8/5.
 */
public class EchoService extends CommonService {


    public static void main(String[] args) throws Exception {

        new EchoService().run();
    }

    @Override
    ChannelHandler getChannelHandler() {

        return new EchoServiceHandler();
    }
}