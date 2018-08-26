package com.asa.spark.rpc.utils;

import com.asa.spark.rpc.expection.SparkRunTimeException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public class CommonUtils {

    /**
     * 如果是source是以prefix开头则进行去掉
     *
     * @param source
     * @param prefix
     * @return
     */
    public static String stripPrefix(String source, String prefix) {

        if (prefix != null && source.startsWith(prefix)) {
            return source.substring(prefix.length());
        }
        return source;
    }

    /**
     * 如果是source是以suffix结尾则进行去掉
     *
     * @param source
     * @param suffix
     * @return
     */
    public static String stripSuffix(String source, String suffix) {

        if (suffix != null && source.endsWith(suffix)) {
            return source.substring(0, source.length() - suffix.length());
        }
        return source;
    }

    /**
     * 校验
     *
     * @param req
     * @param errorMsg
     */
    public static void require(boolean req, String errorMsg) {

        if (!req) {
            throw new SparkRunTimeException(errorMsg);
        }
    }

    /**
     * 校验
     *
     * @param req
     * @param errorMsgPatter
     */
    public static void require(boolean req, String errorMsgPatter, Object... args) {

        if (!req) {
            throw new SparkRunTimeException(String.format(errorMsgPatter, args));
        }
    }

    public static void require(boolean req) {

        if (!req) {
            throw new SparkRunTimeException();
        }
    }

    public static boolean allNotNull(Object... args) {

        for (Object arg : args) {
            if (arg == null) {
                return false;
            }
        }
        return true;
    }

    public static <T> List<T> asList(T... els) {

        List<T> ret = new ArrayList<T>();
        for (T e : els) {
            ret.add(e);
        }
        return ret;
    }


    public static <T> List<T> enumerationToList(Enumeration<T> enumeration) {

        List<T> ret = new ArrayList<T>();
        while (enumeration.hasMoreElements()) {
            ret.add(enumeration.nextElement());
        }
        return ret;

    }

    /**
     * A file name may contain some invalid URI characters, such as " ". This method will convert the
     * file name to a raw path accepted by `java.net.URI(String)`.
     * <p>
     * Note: the file name must not contain "/" or "\"
     */
    public static String encodeFileNameToURIRawPath(String fileName) {

        require(!fileName.contains("/") && !fileName.contains("\\"));
        // `file` and `localhost` are not used. Just to prevent URI from parsing `fileName` as
        // scheme or host. The prefix "/" is required because URI doesn't accept a relative path.
        // We should remove it after we get the raw path.
        URI uri = null;
        try {
            uri = new URI("file", null, "localhost", -1, "/" + fileName, null, null);
        } catch (URISyntaxException e) {
            throw new SparkRunTimeException("invalid fileName %s encodeFileNameToURIRawPath", fileName);
        }
        return uri.getRawPath().substring(1);
    }

    /**
     * Get the file name from uri's raw path and decode it. If the raw path of uri ends with "/",
     * return the name before the last "/".
     */
    public static String decodeFileNameInURI(URI uri) throws URISyntaxException {

        String rawPath = uri.getRawPath();
        String[] rps = rawPath.split("/");
        String rawFileName = rps[rps.length - 1];
        URI ur = new URI("file:///" + rawFileName);
        return ur.getPath().substring(1);
    }
}
