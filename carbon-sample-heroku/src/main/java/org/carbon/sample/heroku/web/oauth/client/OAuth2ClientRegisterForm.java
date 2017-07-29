package org.carbon.sample.heroku.web.oauth.client;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author garden 2017/07/21.
 */
@Getter
@Setter
public class OAuth2ClientRegisterForm {
    @NotBlank
    private String clientHost;
}
