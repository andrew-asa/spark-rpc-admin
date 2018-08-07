package com.asa.spark.rpc.internalimp.netty;

import com.asa.spark.rpc.internalimp.endpoint.RpcEndpoint;

import java.util.Objects;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public class EndpointData {

    private String name;

    private RpcEndpoint endpoint;

    private NettyRpcEndpointRef ref;

    private Inbox inbox;

    public EndpointData(String name, RpcEndpoint endpoint, NettyRpcEndpointRef ref) {

        this.name = name;
        this.endpoint = endpoint;
        this.ref = ref;
        inbox = new Inbox(ref, endpoint);
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public RpcEndpoint getEndpoint() {

        return endpoint;
    }

    public void setEndpoint(RpcEndpoint endpoint) {

        this.endpoint = endpoint;
    }

    public NettyRpcEndpointRef getRef() {

        return ref;
    }

    public void setRef(NettyRpcEndpointRef ref) {

        this.ref = ref;
    }

    public Inbox getInbox() {

        return inbox;
    }

    public void setInbox(Inbox inbox) {

        this.inbox = inbox;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (!(o instanceof EndpointData)) {
            return false;
        }
        EndpointData data = (EndpointData) o;
        return Objects.equals(name, data.name) &&
                Objects.equals(endpoint, data.endpoint) &&
                Objects.equals(ref, data.ref);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, endpoint, ref);
    }
}
