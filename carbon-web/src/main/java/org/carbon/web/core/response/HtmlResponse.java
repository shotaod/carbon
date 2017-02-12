package org.carbon.web.core.response;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Shota Oda 2016/10/14.
 */
public class HtmlResponse{
    private String htmlPath;
    private Map<String, Object> data;

    public HtmlResponse(String htmlPath) {
        this.htmlPath = htmlPath;
        this.data = new HashMap<>();
    }

    public HtmlResponse(String htmlPath, Map<String, Object> data) {
        this.htmlPath = htmlPath;
        this.data = data;
    }

    public void putData(String key, Object value) {
        this.data.put(key, value);
    }

    public Map<String, Object> getData() {
        return data;
    }

    public String getHtmlPath() {
        return htmlPath;
    }
}
