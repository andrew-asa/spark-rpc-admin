package com.asa.spark.rpc.internalimp.common.network.server;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */

import com.asa.spark.rpc.internalimp.common.network.client.TransportClient;
import io.netty.channel.Channel;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * The StreamManager is used to fetch individual chunks from a stream. This is used in
 * {@link TransportRequestHandler} in order to respond to fetchChunk() requests. Creation of the
 * stream is outside the scope of the transport layer, but a given stream is guaranteed to be read
 * by only one client connection, meaning that getChunk() for a particular stream will be called
 * serially and that once the connection associated with the stream is closed, that stream will
 * never be used again.
 */
public abstract class StreamManager {
    /**
     * Called in response to a fetchChunk() request. The returned buffer will be passed as-is to the
     * client. A single stream will be associated with a single TCP connection, so this method
     * will not be called in parallel for a particular stream.
     *
     * Chunks may be requested in any order, and requests may be repeated, but it is not required
     * that implementations support this behavior.
     *
     * The returned ManagedBuffer will be release()'d after being written to the network.
     *
     * @param streamId id of a stream that has been previously registered with the StreamManager.
     * @param chunkIndex 0-indexed chunk of the stream that's requested
     */
    public abstract ManagedBuffer getChunk(long streamId, int chunkIndex);
    /**
     * This interface provides an immutable view for data in the form of bytes. The implementation
     * should specify how the data is provided:
     *
     * - {@link FileSegmentManagedBuffer}: data backed by part of a file
     * - {@link NioManagedBuffer}: data backed by a NIO ByteBuffer
     * - {@link NettyManagedBuffer}: data backed by a Netty ByteBuf
     *
     * The concrete buffer implementation might be managed outside the JVM garbage collector.
     * For example, in the case of {@link NettyManagedBuffer}, the buffers are reference counted.
     * In that case, if the buffer is going to be passed around to a different thread, retain/release
     * should be called.
     */
    public abstract class ManagedBuffer {

        /** Number of bytes of the data. */
        public abstract long size();

        /**
         * Exposes this buffer's data as an NIO ByteBuffer. Changing the position and limit of the
         * returned ByteBuffer should not affect the content of this buffer.
         */
        // TODO: Deprecate this, usage may require expensive memory mapping or allocation.
        public abstract ByteBuffer nioByteBuffer() throws IOException;

        /**
         * Exposes this buffer's data as an InputStream. The underlying implementation does not
         * necessarily check for the length of bytes read, so the caller is responsible for making sure
         * it does not go over the limit.
         */
        public abstract InputStream createInputStream() throws IOException;

        /**
         * Increment the reference count by one if applicable.
         */
        public abstract ManagedBuffer retain();

        /**
         * If applicable, decrement the reference count by one and deallocates the buffer if the
         * reference count reaches zero.
         */
        public abstract ManagedBuffer release();

        /**
         * Convert the buffer into an Netty object, used to write the data out. The return value is either
         * a {@link io.netty.buffer.ByteBuf} or a {@link io.netty.channel.FileRegion}.
         *
         * If this method returns a ByteBuf, then that buffer's reference count will be incremented and
         * the caller will be responsible for releasing this new reference.
         */
        public abstract Object convertToNetty() throws IOException;
    }
    /**
     * Called in response to a stream() request. The returned data is streamed to the client
     * through a single TCP connection.
     *
     * Note the <code>streamId</code> argument is not related to the similarly named argument in the
     * {@link #getChunk(long, int)} method.
     *
     * @param streamId id of a stream that has been previously registered with the StreamManager.
     * @return A managed buffer for the stream, or null if the stream was not found.
     */
    public ManagedBuffer openStream(String streamId) {
        throw new UnsupportedOperationException();
    }

    /**
     * Associates a stream with a single client connection, which is guaranteed to be the only reader
     * of the stream. The getChunk() method will be called serially on this connection and once the
     * connection is closed, the stream will never be used again, enabling cleanup.
     *
     * This must be called before the first getChunk() on the stream, but it may be invoked multiple
     * times with the same channel and stream id.
     */
    public void registerChannel(Channel channel, long streamId) { }

    /**
     * Indicates that the given channel has been terminated. After this occurs, we are guaranteed not
     * to read from the associated streams again, so any state can be cleaned up.
     */
    public void connectionTerminated(Channel channel) { }

    /**
     * Verify that the client is authorized to read from the given stream.
     *
     * @throws SecurityException If client is not authorized.
     */
    public void checkAuthorization(TransportClient client, long streamId) { }

    /**
     * Return the number of chunks being transferred and not finished yet in this StreamManager.
     */
    public long chunksBeingTransferred() {
        return 0;
    }

    /**
     * Called when start sending a chunk.
     */
    public void chunkBeingSent(long streamId) { }

    /**
     * Called when start sending a stream.
     */
    public void streamBeingSent(String streamId) { }

    /**
     * Called when a chunk is successfully sent.
     */
    public void chunkSent(long streamId) { }

    /**
     * Called when a stream is successfully sent.
     */
    public void streamSent(String streamId) { }

}
