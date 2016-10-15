package org.dabuntu.web.context;

import java.util.Map;

/**
 * @author ubuntu 2016/10/08.
 */
public class InstanceContainer {
	private Map<Class, Object> instances;

	public InstanceContainer(Map<Class, Object> instances) {
		this.instances = instances;
	}

	public Map<Class, Object> getInstances() {
		return instances;
	}

	public <T> void set(Object object, Class<T> typeAs) {
		boolean assignableFrom = typeAs.isAssignableFrom(object.getClass());
		if (!assignableFrom) throw new ClassCastException();
		this.instances.put(typeAs, object);
	}

	public void set(Object object) {
		this.instances.put(object.getClass(), object);
	}

	@SuppressWarnings("unchecked")
	public <T> T getInstanceByType(Class<T> type) {
		return (T)this.instances.get(type);
	}
}
