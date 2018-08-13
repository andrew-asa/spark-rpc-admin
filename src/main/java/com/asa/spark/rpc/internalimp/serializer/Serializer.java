package com.asa.spark.rpc.internalimp.serializer;

/**
 * @author andrew_asa
 * @date 2018/8/12.
 */
public interface Serializer {

    /** Creates a new [[SerializerInstance]]. */
    SerializerInstance newInstance();
}
