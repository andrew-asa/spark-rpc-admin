package com.asa.spark.rpc.expection;

import com.asa.spark.rpc.internalimp.conf.ShutdownHookManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author andrew_asa
 * @date 2018/8/26.
 */
public class SparkUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private boolean exitOnUncaughtException;

    private Logger LOGGER = LoggerFactory.getLogger(SparkUncaughtExceptionHandler.class);

    public SparkUncaughtExceptionHandler(boolean exitOnUncaughtException) {

        this.exitOnUncaughtException = exitOnUncaughtException;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        try {
            // Make it explicit that uncaught exceptions are thrown when container is shutting down.
            // It will help users when they analyze the executor logs
            String inShutdownMsg = null;
            if (ShutdownHookManager.inShutdown()){
                inShutdownMsg = "[Container in shutdown] ";
            } else{
                inShutdownMsg = "";
            }
            String errMsg = "Uncaught exception in thread ";
            LOGGER.error(inShutdownMsg + errMsg + thread, exception);

            // We may have been called from a shutdown hook. If so, we must not call System.exit().
            // (If we do, we will deadlock.)
            //if (!ShutdownHookManager.inShutdown()) {
            //    exception match {
            //        case _: OutOfMemoryError =>
            //            System.exit(SparkExitCode.OOM)
            //        case e: SparkFatalException if e.throwable.isInstanceOf[OutOfMemoryError] =>
            //            // SPARK-24294: This is defensive code, in case that SparkFatalException is
            //            // misused and uncaught.
            //            System.exit(SparkExitCode.OOM)
            //        case _ if exitOnUncaughtException =>
            //            System.exit(SparkExitCode.UNCAUGHT_EXCEPTION)
            //    }
            //}
        } catch(Exception e) {
            //case oom: OutOfMemoryError => Runtime.getRuntime.halt(SparkExitCode.OOM)
            //case t: Throwable => Runtime.getRuntime().halt(SparkExitCode.UNCAUGHT_EXCEPTION_TWICE)
        }
    }
}
