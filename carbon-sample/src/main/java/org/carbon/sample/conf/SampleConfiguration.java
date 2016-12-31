package org.carbon.sample.conf;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Configuration;
import org.carbon.component.annotation.Inject;
import org.carbon.web.conf.ConfigHolder;
import org.carbon.web.context.session.RedisSessionStore;
import org.carbon.web.context.session.SessionStore;

/**
 * @author ubuntu 2016/12/17.
 */
@Configuration
public class SampleConfiguration {
    @Inject
    ConfigHolder config;
    @Component
    public SessionStore redisSession() {
        RedisConfig redis = config.findOne("sample.redis", RedisConfig.class);
        return new RedisSessionStore(redis.getHost(), redis.getPort());
    }
}
