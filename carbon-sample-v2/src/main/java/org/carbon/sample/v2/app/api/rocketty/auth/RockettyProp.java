package org.carbon.sample.v2.app.api.rocketty.auth;

import lombok.Getter;
import lombok.Setter;
import org.carbon.modular.annotation.Property;

/**
 * @author Shota.Oda 2018/02/15.
 */
@Getter
@Setter
@Property(key = "rocketty")
public class RockettyProp {
    private String secret;
}
