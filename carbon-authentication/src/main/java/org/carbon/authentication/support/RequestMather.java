package org.carbon.authentication.support;

import javax.servlet.http.HttpServletRequest;

import org.carbon.web.def.HttpMethod;

/**
 * @author garden 2018/02/12.
 */
public class RequestMather {
    private final static String ANY_PATH = "**";
    private HttpMethod method;
    private boolean include;
    private String path;

    public RequestMather(HttpMethod method, String path) {
        this.method = method;
        if (path.endsWith(ANY_PATH)) {
            this.path = path.replace(ANY_PATH, "");
            this.include = true;
            return;
        }
        this.path = path;
        this.include = false;
    }

    public boolean isMatch(HttpServletRequest request) {
        if (method != HttpMethod.codeOf(request.getMethod())) {
            return false;
        }
        String requestPath = request.getPathInfo();
        if (requestPath == null) {
            return path == null;
        }
        if (include) {
            return requestPath.startsWith(path);
        }
        return requestPath.equals(path);
    }
}
