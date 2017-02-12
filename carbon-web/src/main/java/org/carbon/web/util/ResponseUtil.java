package org.carbon.web.util;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Shota Oda 2016/11/03.
 */
public class ResponseUtil {
    private final static String Header_Location = "Location";
    public static void redirect(HttpServletResponse response, String url) {
        response.setStatus(HttpServletResponse.SC_FOUND);
        response.setHeader(Header_Location, url);
    }
}
