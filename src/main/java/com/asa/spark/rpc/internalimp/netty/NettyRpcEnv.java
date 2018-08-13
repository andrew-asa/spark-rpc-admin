package com.asa.spark.rpc.internalimp.netty;

import com.asa.spark.rpc.internalimp.addr.RpcAddress;
import com.asa.spark.rpc.internalimp.common.network.client.TransportClient;
import com.asa.spark.rpc.internalimp.common.network.client.TransportClientBootstrap;
import com.asa.spark.rpc.internalimp.common.network.client.TransportClientFactory;
import com.asa.spark.rpc.internalimp.common.network.context.TransportContext;
import com.asa.spark.rpc.internalimp.common.network.data.Pair;
import com.asa.spark.rpc.internalimp.common.network.server.NoOpRpcHandler;
import com.asa.spark.rpc.internalimp.common.network.server.TransportServer;
import com.asa.spark.rpc.internalimp.common.network.server.TransportServerBootstrap;
import com.asa.spark.rpc.internalimp.conf.SparkConf;
import com.asa.spark.rpc.internalimp.conf.TransportConf;
import com.asa.spark.rpc.internalimp.endpoint.RpcEndpoint;
import com.asa.spark.rpc.internalimp.endpoint.RpcEndpointRef;
import com.asa.spark.rpc.internalimp.env.RpcEnv;
import com.asa.spark.rpc.internalimp.env.RpcEnvFileServer;
import com.asa.spark.rpc.internalimp.netty.msg.OutboxMessage;
import com.asa.spark.rpc.internalimp.netty.msg.in.RequestMessage;
import com.asa.spark.rpc.internalimp.netty.msg.out.OneWayOutboxMessage;
import com.asa.spark.rpc.internalimp.serializer.JavaSerializerInstance;
import com.asa.spark.rpc.internalimp.serializer.SerializationStream;
import com.asa.spark.rpc.utils.CommonUtils;
import com.asa.spark.rpc.utils.StringFormatUtils;
import com.asa.spark.rpc.utils.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.ReadableByteChannel;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 * 基于netty 实现的rpc环境
 */
public class NettyRpcEnv extends RpcEnv {

    private SparkConf conf;

    private JavaSerializerInstance javaSerializerInstance;

    private String host;

    private int numUsableCores;

    private Dispatcher dispatcher;

    private NettyStreamManager streamManager = new NettyStreamManager(this);


    private ScheduledExecutorService timeoutScheduler = ThreadUtils.newDaemonSingleThreadScheduledExecutor("netty-rpc-env-timeout");

    // Because TransportClientFactory.createClient is blocking, we need to run it in this thread pool
    // to implement non-blocking send/ask.
    // TODO: a non-blocking TransportClientFactory.createClient in future
    private ThreadPoolExecutor clientConnectionExecutor = ThreadUtils.newDaemonCachedThreadPool(
            "netty-rpc-connection",
            conf.getInt("spark.rpc.connect.threads", 64));

    private TransportServer server;

    private AtomicBoolean stopped = new AtomicBoolean(false);


    private TransportConf transportConf = SparkTransportConf.fromSparkConf(
            conf.clone().set("spark.rpc.io.numConnectionsPerPeer", "1"),
            "rpc",
            conf.getInt("spark.rpc.io.threads", 0));

    private TransportContext transportContext = new TransportContext(transportConf,
                                                                     new NettyRpcHandler(dispatcher, this, streamManager));

    private TransportClientFactory clientFactory = transportContext.createClientFactory(createClientBootstraps());

    //private DynamicVariable currentClient = new DynamicVariable[TransportClient](null)

    private ConcurrentHashMap<RpcAddress, Outbox> outboxes = new ConcurrentHashMap<RpcAddress, Outbox>();

    private TransportClientFactory fileDownloadFactory;

    private com.asa.spark.rpc.internalimp.env.SecurityManager securityManager;


    private static Logger LOGGER = LoggerFactory.getLogger(NettyRpcEnv.class);


    public NettyRpcEnv(SparkConf conf, JavaSerializerInstance javaSerializerInstance, String host, com.asa.spark.rpc.internalimp.env.SecurityManager securityManager, int numUsableCores) {

        this.conf = conf;
        this.javaSerializerInstance = javaSerializerInstance;
        this.host = host;
        this.securityManager = securityManager;
        this.numUsableCores = numUsableCores;
        this.dispatcher = new Dispatcher(this, numUsableCores);
    }

    @Override
    public RpcEndpointRef endpointRef(RpcEndpoint endpoint) {

        return null;
    }

    /**
     * Remove the address's Outbox and stop it.
     */
    public void removeOutbox(RpcAddress address) {

        Outbox outbox = outboxes.remove(address);
        if (outbox != null) {
            outbox.stop();
        }
    }

    public void startServer(String bindAddress, int port) {

        List<TransportServerBootstrap> bootstraps;
        //if (securityManager.isAuthenticationEnabled()) {
        //    java.util.Arrays.asList(new AuthServerBootstrap(transportConf, securityManager))
        //} else {
        //    java.util.Collections.emptyList();
        //}
        bootstraps = Collections.emptyList();
        server = transportContext.createServer(bindAddress, port, bootstraps);
        dispatcher.registerRpcEndpoint(
                RpcEndpointVerifier.NAME, new RpcEndpointVerifier(this, dispatcher));
    }

    @Override
    public void stop(RpcEndpointRef endpointRef) {

        CommonUtils.require(endpointRef instanceof NettyRpcEndpointRef);
        dispatcher.stop(endpointRef);
    }

    private void postToOutbox(NettyRpcEndpointRef receiver, OutboxMessage message) {

        if (receiver.getClient() != null) {
            message.sendWith(receiver.getClient());
        } else {
            CommonUtils.require(receiver.getAddr() != null,
                                "Cannot send message to client endpoint with no listen address.");
            Outbox targetOutbox;
            Outbox outbox = outboxes.get(receiver.getAddr());
            if (outbox == null) {
                Outbox newOutbox = new Outbox(this, receiver.getEndpointAddress().getRpcAddress());
                Outbox oldOutbox = outboxes.putIfAbsent(receiver.getEndpointAddress().getRpcAddress(), newOutbox);
                if (oldOutbox == null) {
                    targetOutbox = newOutbox;
                } else {
                    targetOutbox = oldOutbox;
                }
            } else {
                targetOutbox = outbox;
            }
            if (stopped.get()) {
                // It's possible that we put `targetOutbox` after stopping. So we need to clean it.
                outboxes.remove(receiver.getAddr());
                targetOutbox.stop();
            } else {
                targetOutbox.send(message);
            }
        }
    }

    @Override
    public RpcAddress getAddress() {

        if (server != null) {
            return new RpcAddress(host, server.getPort());
        } else {
            return null;
        }
    }

    public void send(RequestMessage message) {

        RpcAddress remoteAddr = message.getReceiver().getEndpointAddress().getRpcAddress();
        if (remoteAddr.equals(getAddress())) {
            // Message to a local RPC endpoint.
            try {
                dispatcher.postOneWayMessage(message);
            } catch (Exception e) {
                LOGGER.info(e.getMessage(), e);
            }
        } else {
            // Message to a remote RPC endpoint.
            try {
                postToOutbox(message.getReceiver(), new OneWayOutboxMessage(message.serialize(this)));
            } catch (Exception e) {
                LOGGER.info(e.getMessage(), e);
            }
        }
    }

    @Override
    public RpcEndpointRef setupEndpoint(String name, RpcEndpoint endpoint) {

        return dispatcher.registerRpcEndpoint(name, endpoint);
    }

    @Override
    public RpcEnvFileServer fileServer() {

        return null;
    }

    @Override
    public ReadableByteChannel openChannel(String uri) throws Exception {

        URI parsedUri = new URI(uri);
        CommonUtils.require(parsedUri.getHost() != null, "Host name must be defined.");
        CommonUtils.require(parsedUri.getPort() > 0, "Port must be defined.");
        CommonUtils.require(parsedUri.getPath() != null && parsedUri.getPath() != null, "Path must be defined.");

        Pipe pipe = Pipe.open();
        FileDownloadChannel source = new FileDownloadChannel(pipe.source());
        try {
            TransportClient client = downloadClient(parsedUri.getHost(), parsedUri.getPort());
            FileDownloadCallback callback = new FileDownloadCallback(pipe.sink(), source, client);
            client.stream(parsedUri.getPath(), callback);
        } catch (Exception e) {
            LOGGER.info(e.getMessage(), e);
        } finally {
            pipe.sink().close();
            source.close();
        }

        return source;
    }

    public TransportClient downloadClient(String host, int port) throws IOException, InterruptedException {

        if (fileDownloadFactory == null) {
            synchronized (this) {

            }
            if (fileDownloadFactory == null) {
                String module = "files";
                String prefix = "spark.rpc.io.";
                SparkConf clone = conf.clone();

                // Copy any RPC configuration that is not overridden in the spark.files namespace.
                conf.getAll().forEach(new Consumer<Pair<String, String>>() {

                    @Override
                    public void accept(Pair<String, String> stringStringMap) {

                        String key = stringStringMap.getKey();
                        String value = stringStringMap.getValue();
                        if (key.startsWith(prefix)) {
                            String opt = key.substring(prefix.length());
                            clone.setIfMissing(StringFormatUtils.format("spark.%s.io.%s", module, opt), value);
                        }
                    }
                });

                int ioThreads = clone.getInt("spark.files.io.threads", 1);
                TransportConf downloadConf = SparkTransportConf.fromSparkConf(clone, module, ioThreads);
                TransportContext downloadContext = new TransportContext(downloadConf, new NoOpRpcHandler(), true);
                fileDownloadFactory = downloadContext.createClientFactory(createClientBootstraps());
            }
        }
        return fileDownloadFactory.createClient(host, port);
    }

    public SparkConf getConf() {

        return conf;
    }

    public ByteBuffer serialize(Object content) throws Exception {

        return javaSerializerInstance.serialize(content);
    }

    /**
     * Returns [[SerializationStream]] that forwards the serialized bytes to `out`.
     */
    public SerializationStream serializeStream(OutputStream out) {

        return javaSerializerInstance.serializeStream(out);
    }

    public <T> T deserialize(TransportClient client, ByteBuffer bytes) {
        //NettyRpcEnv.currentClient.withValue(client) {
        //    deserialize { () =>
        //        javaSerializerInstance.deserialize[T](bytes)
        //    }
        //}
        return javaSerializerInstance.deserialize(bytes);
    }

    public ThreadPoolExecutor getClientConnectionExecutor() {

        return clientConnectionExecutor;
    }

    public void setClientConnectionExecutor(ThreadPoolExecutor clientConnectionExecutor) {

        this.clientConnectionExecutor = clientConnectionExecutor;
    }

    private List<TransportClientBootstrap> createClientBootstraps() {
        //if (securityManager.isAuthenticationEnabled()) {
        //    return java.util.Arrays.asList(new AuthClientBootstrap(transportConf,
        //                                                    securityManager.getSaslUser(), securityManager));
        //} else {
        //    return java.util.Collections.emptyList();
        //}
        return java.util.Collections.emptyList();
    }

    public TransportClient createClient(RpcAddress address) throws IOException, InterruptedException {

        return clientFactory.createClient(address.getHost(), address.getPort());
    }

    @Override
    public void shutdown() {

        cleanUp();
    }

    @Override
    public void awaitTermination() {

        dispatcher.awaitTermination();
    }

    public void cleanUp() {

        if (!stopped.compareAndSet(false, true)) {
            return;
        }

        Iterator<Outbox> iter = outboxes.values().iterator();
        while (iter.hasNext()) {
            Outbox outbox = iter.next();
            outboxes.remove(outbox.getAddress());
            outbox.stop();
        }
        if (timeoutScheduler != null) {
            timeoutScheduler.shutdownNow();
        }
        if (dispatcher != null) {
            dispatcher.stop();
        }
        if (server != null) {
            server.close();
        }
        if (clientFactory != null) {
            clientFactory.close();
        }
        if (clientConnectionExecutor != null) {
            clientConnectionExecutor.shutdownNow();
        }
        if (fileDownloadFactory != null) {
            fileDownloadFactory.close();
        }
    }
}
