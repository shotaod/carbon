package org.dabuntu.web.context;

import java.util.Map;

/**
 * @author ubuntu 2016/10/05.
 */
public class ApplicationPool {
	// ===================================================================================
	//                                                                                Pool
	//                                                                                ====
	// application scoped
	private static InstanceContainer instancePool;
	private static MappedActionContainer actionPool;

	// session scoped
	private static SessionContainer sessionPool;

	// ===================================================================================
	//
	//                                                                          ==========
	public static ApplicationPool instance = new ApplicationPool();

	private ApplicationPool() {}

	public void setPool(Map<Class, Object> pool) {
		ApplicationPool.instancePool = new InstanceContainer(pool);
	}
	public void setPool(MappedActionContainer mappedActionContainer) {
		ApplicationPool.actionPool = mappedActionContainer;
	}
	public void setPool(SessionContainer sessionPool) {
		ApplicationPool.sessionPool = sessionPool;
	}

	public InstanceContainer getInstancePool() {
		return instancePool;
	}
	public MappedActionContainer getActionPool() {
		return actionPool;
	}
	public SessionContainer getSessionPool() {
		return sessionPool;
	}
}
