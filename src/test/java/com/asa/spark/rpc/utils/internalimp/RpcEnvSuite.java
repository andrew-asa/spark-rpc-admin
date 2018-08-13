package com.asa.spark.rpc.utils.internalimp;

import com.asa.spark.rpc.internalimp.conf.SparkConf;
import com.asa.spark.rpc.internalimp.env.RpcEnv;
import com.asa.spark.rpc.internalimp.env.SparkEnv;
import com.asa.spark.rpc.utils.SparkFunSuite;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;

/**
 * @author andrew_asa
 * @date 2018/8/12.
 */
public abstract class RpcEnvSuite extends SparkFunSuite {

    private RpcEnv env;

    @Before
    public void init() {

        SparkConf conf = new SparkConf();
        env = createRpcEnv(conf, "local", 0);

        SparkEnv sparkEnv = mock(SparkEnv.class);
        when(sparkEnv.getRpcEnv()).thenReturn(env);
        sparkEnv.setEnv(sparkEnv);
    }

    @After
    public void finish() {

    }

    public RpcEnv createRpcEnv(SparkConf conf, String name, int port) {

        return createRpcEnv(conf, name, port, false);
    }


    public abstract RpcEnv createRpcEnv(SparkConf conf, String name, int port, boolean clientModel);

}
