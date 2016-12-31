package org.carbon.web.conf;

import lombok.Data;

/**
 * @author Shota Oda 2016/12/10.
 */
@Data
public class WebConfig {
    private int port;
    private String resourceDirectory;
    private String resourceOutPath;
    private int maxHeaderSize = 512000/*byte*/; // 500KB
    private int maxContentSize = 2097152/*byte*/; // 2MB
}
