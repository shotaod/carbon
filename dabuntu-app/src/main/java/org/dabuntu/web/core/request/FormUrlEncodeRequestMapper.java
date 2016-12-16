package org.dabuntu.web.core.request;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Inject;
import org.dabuntu.util.mapper.NameBasedObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author ubuntu 2016/11/29.
 */
@Component
public class FormUrlEncodeRequestMapper implements TypeSafeRequestMapper {

	private class KeyAndIndex {
		private String key;
		private Integer index;
		public KeyAndIndex(String key, Integer index) {
			this.key = key;
			this.index = index;
		}
		public String getKey() {
			return key;
		}
		public Integer getIndex() {
			return index;
		}
		public boolean isIndexed() {
			return index != null;
		}
	}

	@Inject
	private NameBasedObjectMapper objectMapper;

	@Override
	public <T> T map(HttpServletRequest request, Class<T> mapTo) {

		Map<String, Object> result = new HashMap<>();
		Map<String, String[]> parameterMap = request.getParameterMap();
		parameterMap.entrySet().stream()
			.sorted((o1, o2) -> o1.getKey().compareTo(o2.getKey()))
			.forEach(entry -> {
				String key = entry.getKey();
				String[] value = entry.getValue();
				LinkedList<String> keys = new LinkedList<>();
				keys.addAll(Arrays.asList(key.split("\\.")));
				insertDeep(keys, value, result);
			});

		return objectMapper.map(result, mapTo);
	}

	@SuppressWarnings("unchecked")
	private Object insertDeep(LinkedList<String> keys, String[] value, Map<String, Object> source) {
		KeyAndIndex keyAndIndex = keyAndIndex(keys.poll());
		String key = keyAndIndex.getKey();
		boolean isList = keyAndIndex.isIndexed();

		if (keys.isEmpty()) {
			if (!isList) {
				source.put(key, value[0]);
				return source;
			}
			List list = (List) source.computeIfAbsent(key, absentKey -> {
				return new ArrayList<>();
			});
			list.addAll(Arrays.asList(value));
			return source;
		}

		if (!isList) {
			Map<String, Object> nestMap = (Map) source.computeIfAbsent(key, absentKey -> {
				return new HashMap<>();
			});
			source.put(key, insertDeep(keys, value, nestMap));
			return source;
		} else {
			List list = (List) source.computeIfAbsent(key, absentKey -> {
				return new ArrayList<>();
			});
			Integer index = keyAndIndex.getIndex();
			if (list.size() > index) {
				Map indexedMap = (Map) list.get(index);
				insertDeep(keys, value, indexedMap);
			} else {
				list.add(insertDeep(keys, value, new HashMap<>()));
			}
			source.put(key, list);
			return source;
		}
	}

	private KeyAndIndex keyAndIndex(String key) {
		int indexOf = key.indexOf("[");
		if (indexOf > 0) {
			String name = key.substring(0, indexOf);
			Integer index = Integer.parseInt(key.substring(indexOf, key.length()).replace("[","").replace("]",""));
			return new KeyAndIndex(name, index);
		}
		return new KeyAndIndex(key, null);
	}
}
