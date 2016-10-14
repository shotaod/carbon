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
}
