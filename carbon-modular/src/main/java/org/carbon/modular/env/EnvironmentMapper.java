package org.carbon.modular.env;

import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.carbon.component.annotation.Component;
import org.carbon.util.mapper.YamlObjectMapper;

/**
 * @author Shota Oda 2018/01/01.
 */
@Component
public class EnvironmentMapper {
    private static final String KEY = "profile";
    private static final String BASE_FILE_FORMAT = "config%s.yml";
    private YamlObjectMapper yamlObjectMapper;

    public EnvironmentMapper() {
        String def = "";
        String env = System.getenv(KEY);
        String prop = System.getProperty(KEY);

        URL[] urls = Stream.of(def, env, prop)
                .map(this::getURLByProfile)
                // exclude null path
                .filter(Objects::nonNull)
                .toArray(URL[]::new);
        this.yamlObjectMapper = new YamlObjectMapper(urls);
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

    private URL getURLByProfile(String profile) {
        if (profile == null) return null;
        String ext = profile.isEmpty() ? "" : "." + profile;
        String fileName = String.format(BASE_FILE_FORMAT, ext);
        return Thread.currentThread().getContextClassLoader().getResource(fileName);
    }
}
