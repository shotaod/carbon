package org.carbon.sample.conf;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Configuration;
import org.carbon.component.annotation.Inject;
import org.carbon.util.mapper.PropertyMapper;
import org.carbon.web.context.session.RedisSessionStore;
import org.carbon.web.context.session.SessionStore;

/**
 * @author Shota Oda 2016/12/17.
 */
@Configuration
public class SampleConfiguration {
    @Inject
    private PropertyMapper config;

    @Component
    public SessionStore redisSession() {
        RedisProperty redis = config.findOne("sample.redis", RedisProperty.class).get();
        return new RedisSessionStore(redis.getHost(), redis.getPort());
    }
}
