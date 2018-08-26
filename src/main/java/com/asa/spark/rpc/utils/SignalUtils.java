package com.asa.spark.rpc.utils;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import sun.misc.Signal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author andrew_asa
 * @date 2018/8/26.
 */
public class SignalUtils {

    /**
     * A flag to make sure we only register the logger once.
     */
    private static boolean loggerRegistered = false;

    private static Map<String, ActionHandler> handlers = new HashMap<String, ActionHandler>();

    /**
     * Register a signal handler to log signals on UNIX-like systems.
     */
    public static void registerLogger(Logger log) {

        if (!loggerRegistered) {
            Arrays.asList("TERM", "HUP", "INT").forEach(new Consumer<String>() {

                @Override
                public void accept(String s) {

                    register(s, new Action() {

                        @Override
                        public boolean action() {

                            log.error("RECEIVED SIGNAL " + s);
                            return false;
                        }
                    });
                }
            });
            loggerRegistered = true;
        }
    }

    public static void register(String signal, Action action) {

        if (SystemUtils.IS_OS_UNIX) {
            ActionHandler handler;
            if (!handlers.containsKey(signal)) {
                handler = new ActionHandler(new Signal(signal));
            } else {
                handler = handlers.get(signal);
            }
            handler.register(action);
        }
    }

}
