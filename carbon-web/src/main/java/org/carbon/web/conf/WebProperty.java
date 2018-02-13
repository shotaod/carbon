package org.carbon.web.conf;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.carbon.modular.annotation.Property;
import org.carbon.web.def.HttpMethod;

/**
 * PropertyItem
 * @author Shota Oda 2016/12/10.
 */
@Property(key = "web")
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
    public class Cors {
        private boolean enabled = true;
        private Set<String> allowOrigins = Collections.singleton("*");
        private Set<String> allowMethods = Stream.of(HttpMethod.values()).map(HttpMethod::getCode).collect(Collectors.toSet());
        private Set<String> allowHeaders = Collections.singleton("Content-Type");
        private Integer maxAge = 86400;
        public boolean isEnabled() {
            return enabled;
        }
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        public Set<String> getAllowOrigins() {
            return allowOrigins;
        }
        public void setAllowOrigins(Set<String> allowOrigins) {
            this.allowOrigins = allowOrigins;
        }
        public Set<String> getAllowMethods() {
            return allowMethods;
        }
        public void setAllowMethods(Set<String> allowMethods) {
            this.allowMethods = allowMethods;
        }
        public Set<String> getAllowHeaders() {
            return allowHeaders;
        }
        public void setAllowHeaders(Set<String> allowHeaders) {
            this.allowHeaders = allowHeaders;
        }
        public Integer getMaxAge() {
            return maxAge;
        }
        public void setMaxAge(int maxAge) {
            this.maxAge = maxAge;
        }
    }
    private int port;
    private int maxHeaderSize = 512000/*byte*/; // 500KB
    private int maxContentSize = 2097152/*byte*/; // 2MB
    private CustomError customError;
    private Resource resource;
    private Cors cors = new Cors();

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
    public Cors getCors() {
        return cors;
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
    public void setCors(Cors cors) {
        this.cors = cors;
    }
}
