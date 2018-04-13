package org.carbon.sample.v2.app.api.rocketty.auth.resp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.carbon.web.translate.dto.Json;

/**
 * @author Shota.Oda 2018/02/15.
 */
@Getter
@AllArgsConstructor
public class AuthInfoDTO implements Json {
    private String clientId;
    private String clientSecret;
}
