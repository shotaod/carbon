package org.carbon.web.def;

import java.io.InputStream;

/**
 * @author garden 2017/07/22.
 */
public class Favicon {
    public Favicon() {
        InputStream res = this.getClass().getResourceAsStream("/org/carbon/web/favicon.ico");
        System.out.println(res);
    }
}
