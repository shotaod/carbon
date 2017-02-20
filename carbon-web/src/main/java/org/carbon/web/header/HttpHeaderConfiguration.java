package org.carbon.web.header;

import java.util.Arrays;
import java.util.List;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Configuration;

/**
 * @author Shota Oda 2017/02/17.
 */
@Configuration
public class HttpHeaderConfiguration {
    private final String XContentTypeOptions = "X-Content-Type-Options";
    private final String XContentTypeOptions_Value = "nosniff";

    private final String XXSSProtection = "X-XSS-Protection";
    private final String XXSSProtection_Value = "1; mode=block";

    private final String XFrameOptions = "X-Frame-Options";
    private final String XFrameOptions_Value = "SAMEORIGIN";

    @Component
    public HttpHeaderRegistry httpHeaderRegistry() {
        return new HttpHeaderRegistry(httpHeaders());
    }

    private List<HttpHeader> httpHeaders() {
        return Arrays.asList(
                new HttpHeader(XContentTypeOptions, XContentTypeOptions_Value),
                new HttpHeader(XXSSProtection, XXSSProtection_Value),
                new HttpHeader(XFrameOptions, XFrameOptions_Value)
        );
    }
}
