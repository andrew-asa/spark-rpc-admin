package com.asa.spark.rpc.internalimp.serializer;

import com.asa.spark.rpc.internalimp.conf.SparkConf;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * @author andrew_asa
 * @date 2018/8/12.
 */
public class JavaSerializer implements Serializer, Externalizable {

    private SparkConf sparkConf;

    public JavaSerializer(SparkConf sparkConf) {

        this.sparkConf = sparkConf;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {

    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

    }

    @Override
    public SerializerInstance newInstance() {

        return null;
    }
}
