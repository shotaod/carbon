package org.carbon.sample.conf;

import lombok.Getter;
import lombok.Setter;
import org.carbon.modular.annotation.Property;

/**
 * @author Shota Oda 2016/12/17.
 */
@Setter
@Getter
@Property(key = "sample.redis")
public class RedisProperty {
    private String host;
    private int port;
}
