package com.asa.spark.rpc.internalimp.conf;

/**
 * @author andrew_asa
 * @date 2018/8/26.
 */
public class ShutdownHookManager {

    public static boolean inShutdown() {

        try {
            Thread hook = new Thread();
            // scalastyle:off runtimeaddshutdownhook
            Runtime.getRuntime().addShutdownHook(hook);
            // scalastyle:on runtimeaddshutdownhook
            Runtime.getRuntime().removeShutdownHook(hook);
        } catch (Exception e) {
            return true;
        }
        return false;
    }
}
