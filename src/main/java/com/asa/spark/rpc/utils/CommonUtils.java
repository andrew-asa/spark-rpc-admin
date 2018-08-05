package com.asa.spark.rpc.utils;

import com.asa.spark.rpc.expection.SparkRunTimeException;
import com.google.common.collect.ImmutableMap;

import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author andrew_asa
 * @date 2018/8/5.
 */
public class CommonUtils {

    private static final ImmutableMap<String, TimeUnit> timeSuffixes =
            ImmutableMap.<String, TimeUnit>builder()
                    .put("us", TimeUnit.MICROSECONDS)
                    .put("ms", TimeUnit.MILLISECONDS)
                    .put("s", TimeUnit.SECONDS)
                    .put("m", TimeUnit.MINUTES)
                    .put("min", TimeUnit.MINUTES)
                    .put("h", TimeUnit.HOURS)
                    .put("d", TimeUnit.DAYS)
                    .build();

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
     * Convert a time parameter such as (50s, 100ms, or 250us) to milliseconds for internal use. If
     * no suffix is provided, the passed number is assumed to be in ms.
     */
    public static long timeStringAsMs(String str) {
        return timeStringAs(str, TimeUnit.MILLISECONDS);
    }

    public static long timeStringAs(String str, TimeUnit unit) {
        String lower = str.toLowerCase(Locale.ROOT).trim();

        try {
            Matcher m = Pattern.compile("(-?[0-9]+)([a-z]+)?").matcher(lower);
            if (!m.matches()) {
                throw new NumberFormatException("Failed to parse time string: " + str);
            }

            long val = Long.parseLong(m.group(1));
            String suffix = m.group(2);

            // Check for invalid suffixes
            if (suffix != null && !timeSuffixes.containsKey(suffix)) {
                throw new NumberFormatException("Invalid suffix: \"" + suffix + "\"");
            }

            // If suffix is valid use that, otherwise none was provided and use the default passed
            return unit.convert(val, suffix != null ? timeSuffixes.get(suffix) : unit);
        } catch (NumberFormatException e) {
            String timeError = "Time must be specified as seconds (s), " +
                    "milliseconds (ms), microseconds (us), minutes (m or min), hour (h), or day (d). " +
                    "E.g. 50s, 100ms, or 250us.";

            throw new NumberFormatException(timeError + "\n" + e.getMessage());
        }
    }
}
