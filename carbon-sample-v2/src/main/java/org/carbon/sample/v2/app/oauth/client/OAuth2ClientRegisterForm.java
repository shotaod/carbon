package org.carbon.sample.v2.app.oauth.client;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Shota Oda 2017/07/21.
 */
@Getter
@Setter
public class OAuth2ClientRegisterForm {
    @NotBlank
    private String clientHost;
}
