package org.carbon.util.mapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.carbon.util.SimpleKeyValue;
import org.carbon.util.format.BoxedTitleMessage;
import org.carbon.util.format.ChapterAttr;
import org.carbon.util.exception.ConfigMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

/**
 * @author Shota Oda 2016/11/12.
 */
public class ConfigHolder {
    private Logger logger = LoggerFactory.getLogger(ConfigHolder.class);

    private Yaml yaml;
    private String configFileName;
    private Map<String, Object> config;
    private List<Config> flatConfig;
    private Map<String, Optional> readCache;

    public class Config {
        private String key;
        private Object value;
        private Throwable error;

        public Config(Throwable error) {
            this.error = error;
        }
        public Config(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }
        public Object getValue() {
            return value;
        }
        public Throwable getError() {
            return error;
        }

        public void setKey(String key) {
            this.key = key;
        }
        public void setValue(Object value) {
            this.value = value;
        }
    }

    @SuppressWarnings("unchecked")
    public ConfigHolder(String configFileName) {
        this.configFileName = configFileName;
        this.yaml = new Yaml();
        this.readCache = new HashMap<>();

        try (InputStream stream = getConfigStream()) {
            this.config = Optional.ofNullable(stream)
                    .map(yamlStream -> yaml.loadAs(yamlStream, Map.class))
                    .map(yamlMap -> evaluateEnvironmentVariable((Map<String, Object>)yamlMap))
                    .orElse(new HashMap());
            this.flatConfig = deep(config, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (logger.isInfoEnabled()) {
            logResult();
        }
    }

    public void logResult() {
        List<SimpleKeyValue<String, ?>> kvs = flatConfig.stream().map(conf -> {
            Object value;
            if (conf.getError() != null) value = conf.getError();
            else value = conf.getValue();
            return new SimpleKeyValue<>(conf.getKey(), value);
        }).sorted((kvs1,kvs2)->kvs1.getKey().compareTo(kvs2.getKey())).collect(Collectors.toList());
        String boxedLines = BoxedTitleMessage.produceLeft(kvs);
        String info = ChapterAttr.getBuilder("Configuration Result").appendLine(boxedLines).toString();
        logger.info(info);
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> findPrimitive(String key, Class<T> type) {
        return (Optional<T>) readCache.computeIfAbsent(key, k -> flatConfig.stream()
                .filter(conf -> key.equals(conf.getKey()))
                .map(conf -> {
                    try {
                        return type.cast(conf.getValue());
                    } catch (ClassCastException e) {
                        return null;
                    }
                })
                .findFirst());
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> findOne(String key, Class<T> type) {
        return (Optional<T>) readCache.computeIfAbsent(key, k -> {
            List<T> confs = find(key, type);
            if (confs == null || confs.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(confs.get(0));
        });
    }

    public <T> List<T> find(String key, Class<T> type) {
        return findDeep(config, Arrays.asList(key.split("\\.")), type).orElse(new ArrayList<>());
    }

    @SuppressWarnings("unchecked")
    private Map evaluateEnvironmentVariable(Map<String, Object> yamlMap) {
        return yamlMap.entrySet().stream()
                .map(entry -> {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if (value instanceof String) {
                        String str = (String) value;
                        if (str.startsWith("{") && str.endsWith("}")) {
                            String propKey = str.replace("{", "").replace("}", "");
                            String prop;
                            if (propKey.startsWith("$")) {
                                prop = System.getenv(propKey.replace("$",""));
                            } else {
                                prop = System.getProperty(propKey);
                            }
                            if (prop == null) {
                                throw new ConfigMappingException(String.format("Not Found environment variable:[\"%s\"]", propKey));
                            }
                            return new AbstractMap.SimpleEntry<>(key, prop);
                        }
                        return entry;
                    } else if (value instanceof Map) {
                        Map<String, Object> map = (Map<String, Object>) value;
                        Object mapValue = evaluateEnvironmentVariable(map);
                        return new AbstractMap.SimpleEntry<>(key, mapValue);
                    }
                    return entry;
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private InputStream getConfigStream() {
        return ClassLoader.getSystemResourceAsStream(configFileName);
    }

    @SuppressWarnings("unchecked")
    private <T> Optional<List<T>> findDeep(Map source, List<String> keys, Class<T> type) {
        String key = keys.get(0);
        Object nullableValue = source.get(key);
        Optional<Object> sourceValue = Optional.ofNullable(nullableValue);
        if (keys.size() > 1 && nullableValue instanceof Map) {
            return sourceValue.map(d -> findDeep((Map)d, keys.subList(1, keys.size()), type).orElse(null));
        }

        NameBasedObjectMapper mapper = new NameBasedObjectMapper(false);
        return sourceValue.map(value -> {
            if (value instanceof List) {
                return (List<T>)((List) value).stream()
                        .filter(d -> d instanceof Map)
                        .map(d -> {
                            Map map = (Map) ((Map) d).get(keys.get(1));
                            return mapper.map(map, type);
                        })
                        .collect(Collectors.toList());
            }

            if (type.isAssignableFrom(sourceValue.getClass())) {
                return Collections.singletonList(type.cast(sourceValue));
            }

            if (value instanceof Map) {
                return Collections.singletonList(mapper.map((Map)value, type));
            }

            return new ArrayList<T>();
        });
    }

    @SuppressWarnings("unchecked")
    private List<Config> deep(Map<String, Object> map, String prefix) {
        return map.entrySet().stream()
                .flatMap(entry -> {
                    String entryKey = entry.getKey();
                    final String key = Optional.ofNullable(prefix).map(pre -> pre + '.' + entry.getKey()).orElse(entryKey);
                    Object value = entry.getValue();
                    if (value == null
                            || value instanceof String
                            || value instanceof Integer
                            || value instanceof Boolean
                            || value instanceof Character) {
                        return Collections.singleton(new Config(key, value)).stream();
                    } else if (value instanceof Map) {
                        return ((List<Config>) deep((Map) value, key)).stream();
                    } else if (value instanceof List) {
                        return (Stream<Config>) ((List) value).stream().flatMap(item -> {
                            if (item instanceof Map) {
                                return (Stream<Config>) deep((Map) item, key).stream();
                            }
                            Config error = new Config(new IllegalArgumentException(item.getClass() + " is not allowed under list\n list can contain Map<K, V> only."));
                            return Collections.singleton(error).stream();
                        });
                    }

                    Config error = new Config(new IllegalArgumentException(value.getClass().toString()));
                    return Collections.singleton(error).stream();
                })
                .collect(Collectors.toList());
    }
}