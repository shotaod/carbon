package org.carbon.sample.web.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Shota Oda 2017/01/08.
 */
@Getter
@AllArgsConstructor
public class MessageDto {
    private String sender;
    private String content;
}
