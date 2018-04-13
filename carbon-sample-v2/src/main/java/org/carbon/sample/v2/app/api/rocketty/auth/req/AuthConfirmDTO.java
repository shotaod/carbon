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
public class AuthConfirmDTO {
    @NotBlank
    @Length(min = 255, max = 255)
    private String clientId;
    @NotBlank
    @Length(min = 255, max = 255)
    private String clientSecret;
}
