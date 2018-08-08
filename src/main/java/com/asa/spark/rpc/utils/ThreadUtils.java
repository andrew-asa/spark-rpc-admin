package com.asa.spark.rpc.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author andrew_asa
 * @date 2018/8/7.
 */
public class ThreadUtils {

    /**
     * Wrapper over ScheduledThreadPoolExecutor.
     */
    public static ScheduledExecutorService newDaemonSingleThreadScheduledExecutor(String threadName) {

        ThreadFactory threadFactory = new ThreadFactoryBuilder().setDaemon(true).setNameFormat(threadName).build();
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, threadFactory);
        // By default, a cancelled task is not automatically removed from the work queue until its delay
        // elapses. We have to enable it manually.
        executor.setRemoveOnCancelPolicy(true);
        return executor;
    }

    /**
     * Create a cached thread pool whose max number of threads is `maxThreadNumber`. Thread names
     * are formatted as prefix-ID, where ID is a unique, sequentially assigned integer.
     */
    public static ThreadPoolExecutor newDaemonCachedThreadPool(
            String prefix, int maxThreadNumber, int keepAliveSeconds) {

        ThreadFactory threadFactory = namedThreadFactory(prefix);
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                maxThreadNumber, // corePoolSize: the max number of threads to create before queuing the tasks
                maxThreadNumber, // maximumPoolSize: because we use LinkedBlockingDeque, this one is not used
                keepAliveSeconds,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                threadFactory);
        threadPool.allowCoreThreadTimeOut(true);
        return threadPool;
    }

    public static ThreadPoolExecutor newDaemonCachedThreadPool(String prefix, int maxThreadNumber) {

        return newDaemonCachedThreadPool(prefix, maxThreadNumber, 40);
    }

    /**
     * Wrapper over newCachedThreadPool. Thread names are formatted as prefix-ID, where ID is a
     * unique, sequentially assigned integer.
     */
    public static ThreadPoolExecutor newDaemonFixedThreadPool(int nThreads, String prefix) {

        ThreadFactory threadFactory = namedThreadFactory(prefix);
        return (ThreadPoolExecutor) Executors.newFixedThreadPool(nThreads, threadFactory);

    }

    /**
     * Create a thread factory that names threads with a prefix and also sets the threads to daemon.
     */
    public static ThreadFactory namedThreadFactory(String prefix) {

        return new ThreadFactoryBuilder().setDaemon(true).setNameFormat(prefix + "-%d").build();
    }
}
