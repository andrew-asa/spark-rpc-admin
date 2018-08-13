package com.asa.spark.rpc.utils.internalimp.netty;

import com.asa.spark.rpc.internalimp.addr.RpcEndpointAddress;
import com.asa.spark.rpc.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * @author andrew_asa
 * @date 2018/8/12.
 */
public class NettyRpcAddressSuite {

    @Test
    public void tesToString() {
        RpcEndpointAddress addr = new RpcEndpointAddress("localhost", 12345, "test");
        CommonUtils.require(StringUtils.equals(addr.toString(),"spark://test@localhost:12345"));
    }

    @Test
    public void testToStringForClientMode() {
        RpcEndpointAddress addr = new RpcEndpointAddress(null, "test");
        CommonUtils.require(StringUtils.equals(addr.toString(),"spark-client://test"));
    }
}
