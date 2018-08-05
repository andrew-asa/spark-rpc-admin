package com.asa.spark.rpc.demo.netty.service;

import com.asa.spark.rpc.demo.netty.handler.service.CustomEchoHandler;
import io.netty.channel.ChannelHandler;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public class CustomEchoService extends CommonService {

    @Override
    ChannelHandler getChannelHandler() {

        return new CustomEchoHandler();
    }


    public static void main(String[] args) throws Exception {

        new CustomEchoService().run();
    }
}
