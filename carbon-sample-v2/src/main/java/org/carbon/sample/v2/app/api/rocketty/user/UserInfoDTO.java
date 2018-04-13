package org.carbon.sample.v2.app.api.rocketty.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.carbon.web.translate.dto.Json;

/**
 * @author garden 2018/04/05.
 */
@AllArgsConstructor
@Getter
public class UserInfoDTO implements Json {
    private Integer highScore;
    private String displayName;
}
