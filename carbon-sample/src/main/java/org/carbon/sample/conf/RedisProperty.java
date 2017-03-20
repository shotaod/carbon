package org.carbon.sample.conf;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shota Oda 2016/12/17.
 */
@Setter
@Getter
public class RedisProperty {
    private String host;
    private int port;
}
