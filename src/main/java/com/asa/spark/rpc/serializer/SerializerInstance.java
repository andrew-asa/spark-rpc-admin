package com.asa.spark.rpc.serializer;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public interface SerializerInstance {

    ByteBuffer serialize(Object value) throws Exception;

    <T> T deserialize( ByteBuffer bytes);

    <T> T deserialize( ByteBuffer bytes,  ClassLoader loader);

    SerializationStream serializeStream( OutputStream s);

    DeserializationStream deserializeStream(InputStream s);
}
