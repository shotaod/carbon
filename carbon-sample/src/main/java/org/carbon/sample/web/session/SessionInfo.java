package org.carbon.sample.web.session;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * @author Shota Oda 2016/10/01
 */
@Data
public class SessionInfo {
    private String username;
    private LocalDateTime dateTime;
}
