package org.carbon.sample.v2.infra.secured;

import lombok.Getter;
import lombok.Setter;
import org.carbon.modular.annotation.Property;

/**
 * @author garden 2018/03/29.
 */
@Setter
@Getter
@Property(key = "rocketty.cipher")
public class RockettyCipherProperty {
    private String passphrase;

    public RockettyCipherProperty() {
    }
}
