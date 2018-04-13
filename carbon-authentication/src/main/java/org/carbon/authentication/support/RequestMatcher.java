package org.carbon.authentication.support;

import javax.servlet.http.HttpServletRequest;

import org.carbon.util.Describable;
import org.carbon.web.def.HttpMethod;

/**
 * @author Shota.Oda 2018/02/12.
 */
public class RequestMatcher implements Describable {
    private final static String ANY_PATH = "**";
    private HttpMethod method;
    private boolean include;
    private String path;

    public RequestMatcher(HttpMethod method, String path) {
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

    @Override
    public String describe() {
        return String.format("(%s)%s%s", method, path, include ? ANY_PATH : "");
    }
}
