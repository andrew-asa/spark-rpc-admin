package com.asa.spark.rpc.internalimp.common.network.protocol;

import com.asa.spark.rpc.internalimp.common.network.buffer.ManagedBuffer;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public abstract class AbstractResponseMessage extends AbstractMessage implements ResponseMessage {

    protected AbstractResponseMessage(ManagedBuffer body, boolean isBodyInFrame) {
        super(body, isBodyInFrame);
    }

    public abstract ResponseMessage createFailureResponse(String error);
}
