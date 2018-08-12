package com.asa.spark.rpc.internalimp.env;

import com.asa.spark.rpc.internalimp.addr.RpcAddress;
import com.asa.spark.rpc.internalimp.endpoint.RpcEndpointRef;
import com.asa.spark.rpc.internalimp.endpoint.RpcEndpoint;
import com.asa.spark.rpc.internalimp.netty.NettyRpcEndpointRef;
import com.asa.spark.rpc.utils.CommonUtils;

import java.nio.channels.ReadableByteChannel;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 * rpc 上下文环境
 */
public abstract class RpcEnv {

    /**
     * Return RpcEndpointRef of the registered [[RpcEndpoint]]. Will be used to implement
     * [[RpcEndpoint.self]]. Return `null` if the corresponding [[RpcEndpointRef]] does not exist.
     */
    public abstract RpcEndpointRef endpointRef(RpcEndpoint endpoint);

    /**
     * Return the address that [[RpcEnv]] is listening to.
     */
    RpcAddress address;

    /**
     * Register a [[RpcEndpoint]] with a name and return its [[RpcEndpointRef]]. [[RpcEnv]] does not
     * guarantee thread-safety.
     */
    public abstract RpcEndpointRef setupEndpoint(String name, RpcEndpoint endpoint);

    /**
     * Retrieve the [[RpcEndpointRef]] represented by `uri` asynchronously.
     */
    //def asyncSetupEndpointRefByURI(uri:String):Future[RpcEndpointRef]

    /**
     * Retrieve the [[RpcEndpointRef]] represented by `uri`. This is a blocking action.
     */
    //def setupEndpointRefByURI(uri:String):RpcEndpointRef =
    //
    //{
    //    defaultLookupTimeout.awaitResult(asyncSetupEndpointRefByURI(uri))
    //}

    /**
     * Retrieve the [[RpcEndpointRef]] represented by `address` and `endpointName`.
     * This is a blocking action.
     */
    //RpcEndpointRef setupEndpointRef(RpcAddress address, String endpointName):RpcEndpointRef =
    //
    //{
    //    setupEndpointRefByURI(RpcEndpointAddress(address, endpointName).toString)
    //}

    /**
     * Stop [[RpcEndpoint]] specified by `endpoint`.
     */
    public void stop(RpcEndpointRef endpoint) {

    }

    /**
     * Shutdown this [[RpcEnv]] asynchronously. If need to make sure [[RpcEnv]] exits successfully,
     * call [[awaitTermination()]] straight after [[shutdown()]].
     */
    public void shutdown() {

    }

    /**
     * Wait until [[RpcEnv]] exits.
     * <p>
     * TODO do we need a timeout parameter?
     */
    public void awaitTermination() {

    }

    public RpcAddress getAddress() {

        return address;
    }

    /**
     * [[RpcEndpointRef]] cannot be deserialized without [[RpcEnv]]. So when deserializing any object
     * that contains [[RpcEndpointRef]]s, the deserialization codes should be wrapped by this method.
     */
    //def deserialize[
    //
    //T](deserializationAction:()=>T):T

    /**
     * Return the instance of the file server used to serve files. This may be `null` if the
     * RpcEnv is not operating in server mode.
     */
    public abstract RpcEnvFileServer fileServer();

    /**
     * Open a channel to download a file from the given URI. If the URIs returned by the
     * RpcEnvFileServer use the "spark" scheme, this method will be called by the Utils class to
     * retrieve the files.
     *
     * @param uri URI with location of the file.
     */
    public abstract ReadableByteChannel openChannel(String uri)throws Exception;
}


