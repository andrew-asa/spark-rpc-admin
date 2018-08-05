package com.asa.spark.rpc.utils;

import com.asa.spark.rpc.internalimp.addr.RpcAddress;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public class URLUtilsTest {

    @Test
    public void testSparkUrl() throws Exception {

        String url = "spark://localhost:8080";
        RpcAddress extractAddr = URLUtils.extractHostPortFromSparkUrl(url);
        RpcAddress expAddr = new RpcAddress("localhost", 8080);
        Assert.assertEquals(expAddr, extractAddr);
    }
}
