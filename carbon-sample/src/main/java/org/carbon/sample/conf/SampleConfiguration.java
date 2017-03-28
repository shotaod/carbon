package org.carbon.sample.conf;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Configuration;
import org.carbon.component.annotation.Inject;
import org.carbon.web.context.session.RedisSessionStore;
import org.carbon.web.context.session.SessionStore;

/**
 * @author Shota Oda 2016/12/17.
 */
@Configuration
public class SampleConfiguration {
    @Inject
    private RedisProperty redisProperty;

    @Component
    public SessionStore redisSession() {
        return new RedisSessionStore(
                redisProperty.getHost(),
                redisProperty.getPort()
        );
    }
}
