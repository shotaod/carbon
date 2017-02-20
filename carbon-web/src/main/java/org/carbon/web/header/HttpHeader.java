package org.carbon.web.header;

/**
 * @author Shota Oda 2017/02/17.
 */
public class HttpHeader {
    private String key;
    private String value;

    public HttpHeader(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}