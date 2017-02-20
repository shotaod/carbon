package org.carbon.web.conf;

/**
 * Config mapper Property
 * @author Shota Oda 2016/12/10.
 */
public class WebProperty {
    public class CustomError {
        private String notFound;
        private String serverError;
        public void setNotFound(String notFound) {
            this.notFound = notFound;
        }
        public void setServerError(String serverError) {
            this.serverError = serverError;
        }
        public String getNotFound() {
            return notFound;
        }
        public String getServerError() {
            return serverError;
        }
    }
    public class Resource {
        private String directory;
        private String outPath;
        public void setDirectory(String directory) {
            this.directory = directory;
        }
        public void setOutPath(String outPath) {
            this.outPath = outPath;
        }
        public String getDirectory() {
            return directory;
        }
        public String getOutPath() {
            return outPath;
        }
    }
    private int port;
    private int maxHeaderSize = 512000/*byte*/; // 500KB
    private int maxContentSize = 2097152/*byte*/; // 2MB
    private CustomError customError;
    private Resource resource;

    public int getPort() {
        return port;
    }
    public int getMaxHeaderSize() {
        return maxHeaderSize;
    }
    public int getMaxContentSize() {
        return maxContentSize;
    }
    public CustomError getCustomError() {
        return customError;
    }
    public Resource getResource() {
        return resource;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public void setMaxHeaderSize(int maxHeaderSize) {
        this.maxHeaderSize = maxHeaderSize;
    }
    public void setMaxContentSize(int maxContentSize) {
        this.maxContentSize = maxContentSize;
    }
    public void setCustomError(CustomError customError) {
        this.customError = customError;
    }
    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
