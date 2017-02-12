package org.carbon.sample.web.session;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Shota Oda 2016/10/01
 */
@Data
public class SessionInfo {
    private String username;
    private LocalDateTime dateTime;
}
