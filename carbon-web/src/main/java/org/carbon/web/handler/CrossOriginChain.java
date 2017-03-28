package org.carbon.web.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.web.conf.WebProperty;
import org.carbon.web.def.HttpMethod;

/**
 * @author Shota Oda 2016/10/17.
 */
@Component
public class CrossOriginChain extends HttpHandlerChain {
    private static final String Access_Control_Request_Method = "Access-Control-Request-Method";
    private static final String Access_Control_Request_Headers = "Access-Control-Request-Headers";
    private static final String Access_Control_Allow_Origin = "Access-Control-Allow-Origin";
    private static final String Access_Control_Allow_Methods = "Access-Control-Allow-Methods";
    private static final String Access_Control_Allow_Headers = "Access-Control-Allow-Headers";
    private static final String Access_Control_Max_Age = "Access-Control-Max-Age";

    @Inject
    private WebProperty property;
    private Map<String, String> propertyReadCache = new HashMap<>();

    @Override
    protected void chain(HttpServletRequest request, HttpServletResponse response) {
        response.addHeader(Access_Control_Allow_Origin, getAllowOrigin());
        response.addHeader(Access_Control_Allow_Headers, getAllowHeader());
        response.addHeader(Access_Control_Allow_Methods, getAllowMethod());
        response.addHeader(Access_Control_Max_Age, getMaxAge());

        if (HttpMethod.codeOf(request.getMethod()) != HttpMethod.OPTIONS) {
            super.chain(request, response);
        }
    }

    private String getAllowOrigin() {
        return propertyReadCache.computeIfAbsent(Access_Control_Allow_Origin, header -> {
            return property.getCors().getAllowOrigins().stream().collect(Collectors.joining(","));
        });
    }
    private String getAllowMethod() {
        return propertyReadCache.computeIfAbsent(Access_Control_Allow_Methods, header -> {
            return property.getCors().getAllowMethods().stream().collect(Collectors.joining(","));
        });
    }
    private String getAllowHeader() {
        return propertyReadCache.computeIfAbsent(Access_Control_Allow_Headers, header -> {
            return property.getCors().getAllowHeaders().stream().collect(Collectors.joining(","));
        });
    }
    private String getMaxAge() {
        return propertyReadCache.computeIfAbsent(Access_Control_Max_Age, header -> {
            return property.getCors().getMaxAge().toString();
        });
    }
}
