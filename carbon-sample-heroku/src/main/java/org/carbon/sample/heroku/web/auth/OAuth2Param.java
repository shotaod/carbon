package org.carbon.sample.heroku.web.auth;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author garden 2017/07/17.
 */
@Setter
@Getter
public class OAuth2Param {
    @NotBlank
    @Length(min = 50, max = 50)
    private String client_id;
    private String scope = AuthScope.read_profile.getCode();

    public Set<AuthScope> getScopes() {
        return AuthScope.codesOf(Arrays.asList(scope.split(" ")));
    }
}
