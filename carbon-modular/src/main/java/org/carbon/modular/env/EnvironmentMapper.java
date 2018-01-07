package org.carbon.modular.env;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.carbon.component.annotation.Component;
import org.carbon.util.mapper.YamlObjectMapper;

/**
 * @author Shota Oda 2018/01/01.
 */
@Component
public class EnvironmentMapper {
    private static final String KEY = "profile";
    private YamlObjectMapper yamlObjectMapper;

    public EnvironmentMapper() {
        String env = System.getenv(KEY);
        String prop = System.getProperty(KEY);

        String profile = Optional
                .ofNullable(env)
                .orElseGet(() -> Optional.ofNullable(prop).orElse(""));

        this.yamlObjectMapper = new YamlObjectMapper(String.format("config.%s.yml", profile));
    }

    public <T> Optional<T> findPrimitive(String key, Class<T> type) {
        return yamlObjectMapper.findPrimitive(key, type);
    }

    public void apply(String key, Object object) {
        yamlObjectMapper.apply(key, object);
    }

    public <T> T map(String key, Class<T> type) {
        return yamlObjectMapper.map(key, type);
    }

    public <T> Optional<T> mapOptional(String key, Class<T> type) {
        return yamlObjectMapper.mapOptional(key, type);
    }
}
