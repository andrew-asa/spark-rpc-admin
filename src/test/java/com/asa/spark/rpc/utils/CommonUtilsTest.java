package com.asa.spark.rpc.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public class CommonUtilsTest {

    @Test
    public void testStripPrefix() {

        String content = "/abc";
        String sp = CommonUtils.stripPrefix(content, "/");
        Assert.assertEquals(sp, "abc");
        Assert.assertEquals(sp, CommonUtils.stripPrefix(sp, "/"));
    }

    @Test
    public void testStripSuffix() {

        String content = "/abc/";
        String sp = CommonUtils.stripSuffix(content, "/");
        Assert.assertEquals(sp, "/abc");
        Assert.assertEquals("abc", CommonUtils.stripPrefix(sp, "/"));
    }
}
