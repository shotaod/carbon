package org.carbon.sample.heroku.web.auth;

import lombok.Getter;
import lombok.Setter;
import org.carbon.sample.heroku.ext.jooq.tables.pojos.AuthClient;

/**
 * @author garden 2017/07/21.
 */
@Getter
@Setter
public class AuthClientDto {
    private String host;
    private String clientId;

    public AuthClientDto(AuthClient authClient) {
        this.host = authClient.getClientHost();
        this.clientId = authClient.getClientId();
    }
}
