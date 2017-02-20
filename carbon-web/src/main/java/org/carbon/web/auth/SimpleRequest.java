package org.carbon.web.auth;

import org.carbon.web.def.HttpMethod;

/**
 * @author Shota Oda 2017/02/15.
 */
public class SimpleRequest {
    private final String wildCard = "/**";
    private HttpMethod method;
    private String path;

    public SimpleRequest(HttpMethod method, String path) {
        this.method = method;
        this.path = path;
    }

    public boolean isMatch(HttpMethod requestMethod, String requestPath) {
        boolean urlMatch;
        if (path.endsWith(wildCard)) {
            urlMatch = requestPath.startsWith(path.replace(wildCard, ""));
        } else {
            urlMatch = path.equals(requestPath);
        }
        return method.equals(requestMethod) && urlMatch;
    }
}
