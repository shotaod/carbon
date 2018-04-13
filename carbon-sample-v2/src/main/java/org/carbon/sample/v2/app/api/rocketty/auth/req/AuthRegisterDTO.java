package org.carbon.sample.v2.app.api.rocketty.auth.req;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Shota.Oda 2018/02/15.
 */
@Getter
@Setter
public class AuthRegisterDTO {
    @NotBlank
    @Length(min = 32, max = 32)
    private String appSecret;
}
