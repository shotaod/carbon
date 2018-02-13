package org.carbon.sample.v2.web.oauth.def;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Shota Oda 2017/07/30.
 */
@Getter
@AllArgsConstructor
public enum ResponseType {
    code("code"),
    token("token"),
    code_and_token("code_and_token"),;

    private String value;
}
