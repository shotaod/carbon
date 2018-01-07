package org.carbon.sample.v2.web.oauth;

import lombok.Getter;
import lombok.Setter;
import org.carbon.sample.v2.ext.jooq.tables.pojos.AuthClient;

/**
 * @author Shota Oda 2017/07/21.
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
