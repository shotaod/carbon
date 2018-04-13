package org.carbon.sample.v2.app.api.rocketty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.carbon.web.translate.dto.Json;

/**
 * @author garden 2018/03/25.
 */
@AllArgsConstructor
@Getter
public class SuccessMessageDTO implements Json {
    private String message;
}
