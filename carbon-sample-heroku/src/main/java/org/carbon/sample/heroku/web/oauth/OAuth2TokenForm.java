package org.carbon.sample.heroku.web.oauth;

import lombok.Getter;
import lombok.Setter;
import org.carbon.sample.heroku.util.OneOf;
import org.carbon.sample.heroku.web.oauth.def.GrantType;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Shota Oda 2017/07/29.
 */
@Getter
@Setter
public class OAuth2TokenForm {

    @NotBlank
    @OneOf(GrantType.class)
    private String grant_type;
    @NotBlank
    @Length(min = 50, max = 50)
    private String client_id;
    @Length(min = 10, max = 10)
    private String code;
}
