package com.asa.spark.rpc.internalimp.env;

import com.asa.spark.rpc.utils.CommonUtils;

import java.io.File;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public abstract class RpcEnvFileServer {

    /**
     * Adds a file to be served by this RpcEnv. This is used to serve files from the driver
     * to executors when they're stored on the driver's local file system.
     *
     * @param file Local file to serve.
     * @return A URI for the location of the file.
     */
    public abstract String addFile(File file);

    /**
     * Adds a jar to be served by this RpcEnv. Similar to `addFile` but for jars added using
     * `SparkContext.addJar`.
     *
     * @param file Local file to serve.
     * @return A URI for the location of the file.
     */
    public abstract String addJar(File file);

    /**
     * Adds a local directory to be served via this file server.
     *
     * @param baseUri Leading URI path (files can be retrieved by appending their relative
     *                path to this base URI). This cannot be "files" nor "jars".
     * @param path    Path to the local directory.
     * @return URI for the root of the directory in the file server.
     */
    public abstract String addDirectory(String baseUri, File path);

    /**
     * Validates and normalizes the base URI for directories.
     */
    protected String validateDirectoryUri(String baseUri) {

        String fixedBaseUri = "/" + CommonUtils.stripPrefix(baseUri, "/");
        fixedBaseUri = CommonUtils.stripSuffix(fixedBaseUri, "/");
        CommonUtils.require(!"/files".equals(fixedBaseUri) && !"/jars".equals(fixedBaseUri),
                            "Directory URI cannot be /files nor /jars.");
        return fixedBaseUri;
    }
}
