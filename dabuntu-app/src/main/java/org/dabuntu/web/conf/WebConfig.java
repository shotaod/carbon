package org.dabuntu.web.conf;

import lombok.Data;

/**
 * @author ubuntu 2016/12/10.
 */
@Data
public class WebConfig {
    private int port;
    private String resourceDirectory;
    private String resourceOutPath;
}
