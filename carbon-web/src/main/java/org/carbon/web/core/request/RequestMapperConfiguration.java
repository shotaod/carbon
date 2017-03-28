package org.carbon.web.core.request;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Configuration;
import org.carbon.util.mapper.KeyValueMapper;

/**
 * @author ubuntu 2017/03/28.
 */
@Configuration
public class RequestMapperConfiguration {

    @Component
    public KeyValueMapper keyValueMapper() {
        return new KeyValueMapper(true);
    }
}
