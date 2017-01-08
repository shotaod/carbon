package org.carbon.web.container.request;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Shota Oda 2016/10/12.
 */
public class MappedRequestBody {













	private Map<String, Object> requests;

	public MappedRequestBody(Map<String, Object> requests) {
		this.requests = requests;
	}

	public Map<String, Object> getRequests() {
		return requests;
	}

	public Object getValue(String key) {
		return requests.get(key);
	}

	public List<String> getKeys() {
		return requests.keySet().stream().collect(Collectors.toList());
	}
}
