package com.asa.spark.rpc.internalimp.netty;

import com.asa.spark.rpc.internalimp.conf.ConfigProvider;
import com.asa.spark.rpc.internalimp.conf.SparkConf;
import com.asa.spark.rpc.internalimp.conf.TransportConf;
import com.asa.spark.rpc.utils.StringFormatUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author andrew_asa
 * @date 2018/8/8.
 */
public class SparkTransportConf {

    private static int MAX_DEFAULT_NETTY_THREADS = 8;

    /**
     * Utility for creating a [[TransportConf]] from a [[SparkConf]].
     *
     * @param _conf          the [[SparkConf]]
     * @param module         the module name
     * @param numUsableCores if nonzero, this will restrict the server and client threads to only
     *                       use the given number of cores, rather than all of the machine's cores.
     *                       This restriction will only occur if these properties are not already set.
     */
    public static TransportConf fromSparkConf(SparkConf _conf, String module, int numUsableCores) {

        SparkConf conf = _conf.clone();

        // Specify thread configuration based on our JVM's allocation of cores (rather than necessarily
        // assuming we have all the machine's cores).
        // NB: Only set if serverThreads/clientThreads not already set.
        int numThreads = defaultNumThreads(numUsableCores);
        conf.setIfMissing(StringFormatUtils.format("spark.%s.io.serverThreads", module), String.valueOf(numThreads));
        conf.setIfMissing(StringFormatUtils.format("spark.%s.io.clientThreads", module), String.valueOf(numThreads));

        return new TransportConf(module, new ConfigProvider() {

            @Override
            public String get(String name) {

                return conf.get(name);
            }

            @Override
            public Iterator<Map.Entry<String, String>> getAll() {

                Map<String, String> ret = new HashMap<String, String>();
                List<Map<String, String>> all = conf.getAll();
                all.forEach(new Consumer<Map<String, String>>() {

                    @Override
                    public void accept(Map<String, String> stringStringMap) {

                        ret.putAll(stringStringMap);
                    }
                });
                return ret.entrySet().iterator();
            }
        });
    }

    /**
     * Returns the default number of threads for both the Netty client and server thread pools.
     * If numUsableCores is 0, we will use Runtime get an approximate number of available cores.
     */
    private static int defaultNumThreads(int numUsableCores) {

        int availableCores =
                numUsableCores > 0 ? numUsableCores : Runtime.getRuntime().availableProcessors();
        Math.min(availableCores, MAX_DEFAULT_NETTY_THREADS);
        return availableCores;
    }

}
