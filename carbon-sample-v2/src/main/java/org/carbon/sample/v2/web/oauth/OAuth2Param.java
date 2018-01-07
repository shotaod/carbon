package org.carbon.sample.v2.web.oauth;

import java.util.Arrays;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import org.carbon.sample.v2.util.OneOf;
import org.carbon.sample.v2.web.oauth.def.AuthScope;
import org.carbon.sample.v2.web.oauth.def.ResponseType;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Shota Oda 2017/07/17.
 */
@Setter
@Getter
public class OAuth2Param {

    @NotBlank
    @OneOf(ResponseType.class)
    private String response_type;
    @NotBlank
    @Length(min = 50, max = 50)
    private String client_id;
    private String scope = AuthScope.read_profile.getCode();
    @NotBlank
    private String redirect_uri;

    public Set<AuthScope> getScopes() {
        return AuthScope.codesOf(Arrays.asList(scope.split(" ")));
    }
}
