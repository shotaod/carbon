package org.dabuntu.web.context;

import org.dabuntu.web.container.InstanceContainer;
import org.dabuntu.web.container.MappedActionContainer;

import java.util.Map;

/**
 * @author ubuntu 2016/10/05.
 */
public class ApplicationPool {
	private static InstanceContainer instancePool;
	private static MappedActionContainer requestActionPool;

	public static ApplicationPool instance = new ApplicationPool();

	private ApplicationPool() {}

	public void setPool(Map<Class, Object> pool) {
		ApplicationPool.instancePool = new InstanceContainer(pool);
	}
	public void setPool(MappedActionContainer mappedActionContainer) {
		ApplicationPool.requestActionPool = mappedActionContainer;
	}

	public InstanceContainer getInstancePool() {
		return instancePool;
	}
	public MappedActionContainer getRequestActionPool() {
		return requestActionPool;
	}
}
