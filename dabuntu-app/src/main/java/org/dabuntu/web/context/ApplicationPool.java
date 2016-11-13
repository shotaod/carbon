package org.dabuntu.web.context;

import java.util.Map;

/**
 * @author ubuntu 2016/10/05.
 */
public class ApplicationPool {
	// ===================================================================================
	//                                                                                Pool
	//                                                                                ====

	// -----------------------------------------------------
	//                                               application scoped
	//                                               -------
	// base pool
	private static InstanceContainer appInstancePool;
	// separate with app pool for search action easily
	private static MappedActionContainer actionMapPool;
	private static SecurityContainer securityPool;

	// -----------------------------------------------------
	//                                               session scoped
	//                                               -------
	private static SessionContainer sessionPool = SessionContainer.instance;

	// -----------------------------------------------------
	//                                               request scoped
	//                                               -------
	private static RequestContainer requestPool = RequestContainer.instance;

	public static ApplicationPool instance = new ApplicationPool();

	private ApplicationPool() {}

	public void setPool(Map<Class, Object> instances) {
		ApplicationPool.appInstancePool = new InstanceContainer(instances);
	}
	public void setPool(SecurityContainer securityPool) {
		ApplicationPool.securityPool = securityPool;
	}

	public void setPool(MappedActionContainer mappedActionContainer) {
		ApplicationPool.actionMapPool = mappedActionContainer;
	}

	public InstanceContainer getAppPool() {
		return appInstancePool;
	}
	public MappedActionContainer getActionPool() {
		return actionMapPool;
	}
	public SecurityContainer getAuthLogicPool() {
		return securityPool;
	}
	public SessionContainer getSessionPool() {
		return sessionPool;
	}
	public RequestContainer getRequestPool() {
		return requestPool;
	}
}
