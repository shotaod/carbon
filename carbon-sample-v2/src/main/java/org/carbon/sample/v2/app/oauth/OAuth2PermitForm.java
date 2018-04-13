package org.carbon.sample.v2.app.oauth;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import org.carbon.sample.v2.util.OneOf;
import org.carbon.sample.v2.app.oauth.def.AuthScope;
import org.carbon.sample.v2.app.oauth.def.ResponseType;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Shota Oda 2017/07/29.
 */
@Getter
@Setter
public class OAuth2PermitForm {
    private final static Set<String> DEFAULT_SCOPE = Collections.singleton(AuthScope.read_profile.getCode());
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;

    @NotBlank
    @OneOf(ResponseType.class)
    private String response_type;
    @NotBlank
    private String host;
    @NotBlank
    @Length(min = 50, max = 50)
    private String client_id;
    @NotBlank
    private String redirect_uri;
    private Set<String> scope = DEFAULT_SCOPE;

    public Set<AuthScope> getScopes() {
        return scope.stream()
                .map(AuthScope::codeOf)
                .collect(Collectors.toSet());
    }
}
