package org.carbon.sample.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Configuration;
import org.carbon.component.annotation.Assemble;
import org.carbon.web.context.session.store.RedisSessionStore;
import org.carbon.web.context.session.store.SessionStore;

/**
 * @author Shota Oda 2016/12/17.
 */
@Configuration
public class SampleConfiguration {
    @Assemble
    private RedisProperty redisProperty;
    @Assemble
    private ObjectMapper objectMapper;

    @Component
    public SessionStore redisSession() {
        return new RedisSessionStore(
                redisProperty.getHost(),
                redisProperty.getPort(),
                objectMapper
        );
    }
}
