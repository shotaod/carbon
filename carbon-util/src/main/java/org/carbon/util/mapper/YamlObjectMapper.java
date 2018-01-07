package org.carbon.util.mapper;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.carbon.util.SimpleKeyValue;
import org.carbon.util.exception.PropertyMappingException;
import org.carbon.util.exception.ResourceLoadingException;
import org.carbon.util.fn.Fn;
import org.carbon.util.format.BoxedTitleMessage;
import org.carbon.util.format.ChapterAttr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Shota Oda 2016/11/12.
 */
public class YamlObjectMapper {
    private final static Pattern PLACEHOLDER_PATTERN = Pattern.compile("(\\{.*?})");
    private Logger logger = LoggerFactory.getLogger(YamlObjectMapper.class);

    private Yaml yaml;
    private KeyValueMapper mapper;
    private Map<String, Object> config;
    private List<PropertyItem> flatConfig;
    private Map<String, Object> readCache;

    public class PropertyItem {
        private String key;
        private Object value;
        private Throwable error;

        public PropertyItem(Throwable error) {
            this.error = error;
        }

        public PropertyItem(String key, Object value) {
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
    public YamlObjectMapper(String configFile) {
        this.yaml = new Yaml();
        this.mapper = new KeyValueMapper();
        this.readCache = new HashMap<>();

        String configString = getConfigString(configFile);
        Map configMap = yaml.loadAs(configString, Map.class);
        this.config = evaluateEnvironmentVariable(configMap);
            this.flatConfig = flatten(config, null);

        if (logger.isInfoEnabled()) {
            logResult();
        }
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
    public void apply(String key, Object object) {
        Map<String, Object> copy = new HashMap<>(config);
        for (String k : Arrays.asList(key.split("\\."))) {
            try {
                copy = (Map<String, Object>) copy.get(k);
            } catch (NullPointerException e) {
                throw new PropertyMappingException("Not found for key: [" + k + "] declared at " + object.getClass());
            } catch (ClassCastException e) {
                throw new PropertyMappingException("Not compatible structure at key[" + k + "] for " + object.getClass());
            }

        }
        mapper.map(object, copy);
    }

    @SuppressWarnings("unchecked")
    public <T> T map(String key, Class<T> type) {
        return (T) readCache.computeIfAbsent(key, k -> {
            T obj = Fn
                    .Try(() -> ConstructorUtils.invokeExactConstructor(type))
                    .CatchThrow(t -> {
                        throw new PropertyMappingException(t);
                    });
            apply(k, obj);
            return obj;
        });
    }

    public <T> Optional<T> mapOptional(String key, Class<T> type) {
        return Fn
                .Try(() -> Optional.of(map(key, type)))
                .CatchReturn(t -> Optional.empty());
    }

    private void logResult() {
        List<SimpleKeyValue<String, ?>> kvs = flatConfig.stream().map(conf -> {
            Object value;
            if (conf.getError() != null) value = conf.getError();
            else value = conf.getValue();
            return new SimpleKeyValue<>(conf.getKey(), value);
        }).sorted(Comparator.comparing(SimpleKeyValue::getKey)).collect(Collectors.toList());
        String boxedLines = BoxedTitleMessage.produceLeft(kvs);
        String info = ChapterAttr.getBuilder("Configuration Result").appendLine(boxedLines).toString();
        logger.info(info);
    }

    @SuppressWarnings("unchecked")
    private Map evaluateEnvironmentVariable(Map<String, Object> yamlMap) {
        return yamlMap.entrySet().stream()
                .map(entry -> {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if (value instanceof String) {
                        String str = (String) value;
                        if (str.contains("{") && str.contains("}")) {
                            Matcher matcher = PLACEHOLDER_PATTERN.matcher(str);
                            if (matcher.find()) {
                                for (int i = 1; i <= matcher.groupCount(); i++) {
                                    String placeholder = matcher.group(i);
                                    String placeKey = placeholder.replace("{", "").replace("}", "");
                                    String repVal;
                                    if (placeKey.startsWith("$")) {
                                        repVal = System.getenv(placeKey.replace("$", ""));
                                    } else {
                                        repVal = System.getProperty(placeKey);
                                    }
                                    if (repVal == null) {
                                        throw new PropertyMappingException(String.format("Not Found environment variable:[\"%s\"]", placeKey));
                                    }
                                    str = str.replace(placeholder, repVal);
                                }
                            }
                            return new AbstractMap.SimpleEntry<>(key, str);
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

    private String getConfigString(String configFile) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(configFile).toURI()));
            return new String(bytes);
        } catch (Exception e) {
            throw new ResourceLoadingException("Fail to load resource ["+configFile+"]", e);
        }
    }

    @SuppressWarnings("unchecked")
    private List<PropertyItem> flatten(Map<String, Object> map, String prefix) {
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
                        return Stream.of(new PropertyItem(key, value));
                    } else if (value instanceof Map) {
                        return ((List<PropertyItem>) flatten((Map) value, key)).stream();
                    } else if (value instanceof List) {
                        return (Stream<PropertyItem>) ((List) value).stream().flatMap(item -> {
                            if (item instanceof Map) {
                                return (Stream<PropertyItem>) flatten((Map) item, key).stream();
                            }
                            PropertyItem error = new PropertyItem(new IllegalArgumentException(item.getClass() + " is not allowed under list\n list can contain Map<K, V> only."));
                            return Stream.of(error);
                        });
                    }

                    PropertyItem error = new PropertyItem(new IllegalArgumentException(value.getClass().toString()));
                    return Stream.of(error);
                })
                .collect(Collectors.toList());
    }
}