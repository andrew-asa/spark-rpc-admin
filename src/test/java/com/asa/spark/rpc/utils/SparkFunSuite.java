package com.asa.spark.rpc.utils;

import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;

/**
 * @author andrew_asa
 * @date 2018/8/12.
 */
public class SparkFunSuite {

    public static <T> T mock(Class<T> classToMock) {

        return Mockito.mock(classToMock);
    }

    public static <T> OngoingStubbing<T> when(T methodCall) {
        return Mockito.when(methodCall);
    }
}
