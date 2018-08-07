package com.asa.spark.rpc.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author andrew_asa
 * @date 2018/8/7.
 */
public class ThreadUtils {

    public static ThreadPoolExecutor newDaemonFixedThreadPool(int nThreads, String prefix) {

        ThreadFactory threadFactory = namedThreadFactory(prefix);
        return (ThreadPoolExecutor) Executors.newFixedThreadPool(nThreads, threadFactory);

    }

    public static ThreadFactory namedThreadFactory(String prefix) {

        return new ThreadFactoryBuilder().setDaemon(true).setNameFormat(prefix + "-%d").build();
    }
}
