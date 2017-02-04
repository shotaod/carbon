package org.carbon.web.conf;

/**
 * @author Shota Oda 2016/12/10.
 */
public class WebConfig {
    private int port;
    private String resourceDirectory;
    private String resourceOutPath;
    private int maxHeaderSize = 512000/*byte*/; // 500KB
    private int maxContentSize = 2097152/*byte*/; // 2MB

    public int getPort() {
        return port;
    }

    public String getResourceDirectory() {
        return resourceDirectory;
    }

    public String getResourceOutPath() {
        return resourceOutPath;
    }

    public int getMaxHeaderSize() {
        return maxHeaderSize;
    }

    public int getMaxContentSize() {
        return maxContentSize;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setResourceDirectory(String resourceDirectory) {
        this.resourceDirectory = resourceDirectory;
    }

    public void setResourceOutPath(String resourceOutPath) {
        this.resourceOutPath = resourceOutPath;
    }

    public void setMaxHeaderSize(int maxHeaderSize) {
        this.maxHeaderSize = maxHeaderSize;
    }

    public void setMaxContentSize(int maxContentSize) {
        this.maxContentSize = maxContentSize;
    }
}
