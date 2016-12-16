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
}
