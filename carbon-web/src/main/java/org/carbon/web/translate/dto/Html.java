package org.carbon.web.translate.dto;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Shota Oda 2016/10/14.
 */
public class Html implements Translatable {
    private String htmlPath;
    private Map<String, Object> data;
    private String directory = "templates";

    public Html(String htmlPath) {
        this.htmlPath = htmlPath;
        this.data = new HashMap<>();
    }

    public Html(String htmlPath, Map<String, Object> data) {
        this.htmlPath = htmlPath;
        this.data = data;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public void putData(String key, Object value) {
        this.data.put(key, value);
    }

    public Map<String, Object> getData() {
        return data;
    }

    public String getHtmlPath() {
        return Paths.get(directory, htmlPath).toString();
    }
}
