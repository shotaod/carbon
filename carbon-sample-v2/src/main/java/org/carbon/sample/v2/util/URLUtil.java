package org.carbon.sample.v2.util;

/**
 * @author Shota Oda 2017/07/30.
 */
public class URLUtil {
    public static String appendParameter(String url, String key, String value) {
        boolean existParam = url.contains("?");
        if (existParam) {
            return url.concat("&" + key + "=" + value);
        }
        return url.concat("?" + key + "=" + value);
    }
}
