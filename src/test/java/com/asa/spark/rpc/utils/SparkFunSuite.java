package com.asa.spark.rpc.utils;

import org.mockito.Mockito;

/**
 * @author andrew_asa
 * @date 2018/8/12.
 */
public class SparkFunSuite {

    public static <T> T mock(Class<T> classToMock) {

        return Mockito.mock(classToMock);
    }
}
