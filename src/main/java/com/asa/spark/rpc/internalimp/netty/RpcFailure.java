package com.asa.spark.rpc.internalimp.netty;

/**
 * @author andrew_asa
 * @date 2018/8/7.
 */
public class RpcFailure {

    private Throwable e;

    public RpcFailure(Throwable e) {

        this.e = e;
    }

    public Throwable getE() {

        return e;
    }

    public void setE(Throwable e) {

        this.e = e;
    }
}
