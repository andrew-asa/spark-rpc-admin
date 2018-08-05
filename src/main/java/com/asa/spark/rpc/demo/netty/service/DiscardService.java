package com.asa.spark.rpc.demo.netty.service;

import com.asa.spark.rpc.demo.netty.handler.service.DiscardServerHandler;
import io.netty.channel.ChannelHandler;

/**
 * Created by andrew_asa on 2018/8/5.
 */
public class DiscardService extends CommonService {


    public static void main(String[] args) throws Exception {

        new DiscardService().run();
    }

    @Override
    ChannelHandler getChannelHandler() {

        return new DiscardServerHandler();
    }
}
