package org.carbon.authentication.request;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import org.carbon.web.def.HttpMethod;

/**
 * @author Shota Oda 2017/02/15.
 */
public class SimpleRequest {
    private static final String wildCard = "/**";
    private HttpServletRequest baseRequest;
    private HttpMethod method;
    private String path;

    public SimpleRequest(HttpServletRequest request) {
        this.baseRequest = request;
        this.method = HttpMethod.valueOf(request.getMethod().toUpperCase());
        this.path = Optional.ofNullable(request.getPathInfo()).orElse("");
    }

    public SimpleRequest(HttpMethod method, String path) {
        this.method = method;
        this.path = path;
    }

    public HttpServletRequest getBaseRequest() {
        return baseRequest;
    }
    public HttpMethod getMethod() {
        return method;
    }
    public String getPath() {
        return path;
    }

    public boolean isMatch(SimpleRequest request) {
        return this.isMatch(request.getMethod(), request.getPath());
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
