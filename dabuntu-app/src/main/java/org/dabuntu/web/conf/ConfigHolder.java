package org.dabuntu.web.conf;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.dabuntu.util.format.ChapterAttr;
import org.dabuntu.util.mapper.NameBasedObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ubuntu 2016/11/12.
 */
public class ConfigHolder {
	private Logger logger = LoggerFactory.getLogger(ConfigHolder.class);

	private Map<String, Object> config;
	private List<Config> flatConfig;

	@Data
	@NoArgsConstructor
	public class Config {
		private String key;
		private Object value;
		private Throwable error;
		public Config(String key, Object value) {
			this.key = key;
			this.value = value;
		}
	}

	@SuppressWarnings("unchecked")
	public ConfigHolder(String configFileName) {
		Yaml yamlParser = new Yaml();
		this.config = Optional.ofNullable(ClassLoader.getSystemResourceAsStream(configFileName))
				.map(yamlStream -> yamlParser.loadAs(yamlStream, Map.class))
				.orElse(new HashMap());
		this.flatConfig = deep(config, null);
		this.flatConfig.forEach(flat -> logger.debug(flat.toString()));
	}

	public <T> Optional<T> findPrimitive(String key, Class<T> type) {
		return flatConfig.stream()
				.filter(conf -> key.equals(conf.getKey()))
				.map(conf -> type.cast(conf.getValue()))
				.findFirst();
	}

	public <T> List<T> find(String key, Class<T> type) {
		List<T> confs = findDeep(config, Arrays.asList(key.split("\\.")), type).orElse(new ArrayList<>());

		String chapter = ChapterAttr.get("Configuration Result");
		String results = confs.stream().map(conf -> conf.toString()).collect(Collectors.joining("\n"));

		logger.debug(chapter + results + "\n");

		return confs;
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

			return new ArrayList<T>();
		});
	}

	@SuppressWarnings("unchecked")
	public List<Config> deep(Map<String, Object> map, String prefix) {
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
						Config error = new Config();
						return Collections.singleton(error).stream();
					});
				}

				Config error = new Config();
				error.setError(new IllegalArgumentException(value.getClass().toString()));
				return Collections.singleton(error).stream();
			})
			.collect(Collectors.toList());
	}
}
