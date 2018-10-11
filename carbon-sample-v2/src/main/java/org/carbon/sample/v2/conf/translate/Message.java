package org.carbon.sample.v2.conf.translate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.carbon.web.translate.dto.Json;

/**
 * @author garden 2018/04/14.
 */
@AllArgsConstructor
@Getter
@Setter
class Message implements Json {
    private String message;
}
