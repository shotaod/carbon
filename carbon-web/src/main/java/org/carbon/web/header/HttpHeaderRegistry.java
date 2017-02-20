package org.carbon.web.header;

import java.util.List;

/**
 * @author Shota Oda 2017/02/17.
 */
public class HttpHeaderRegistry {

    private List<HttpHeader> httpHeaders;

    public HttpHeaderRegistry(List<HttpHeader> httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    public List<HttpHeader> getHttpHeaders() {
        return httpHeaders;
    }
}
