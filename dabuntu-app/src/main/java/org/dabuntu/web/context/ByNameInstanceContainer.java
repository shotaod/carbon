package org.dabuntu.web.context;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ubuntu 2016/10/27.
 */
public class ByNameInstanceContainer {
	private Map<String, Object> instances;

	public ByNameInstanceContainer() {
		this.instances = new HashMap<>();
	}

	public ByNameInstanceContainer(Map<String, Object> instances) {
		this.instances = instances;
	}

	public Map<String, Object> getAll() {
		return instances;
	}

	public void set(String key, Object value) {
		this.instances.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		return (T)this.instances.get(key);
	}
}
