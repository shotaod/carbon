package org.dabuntu.web.context;

import java.util.HashMap;

/**
 * @author ubuntu 2016/10/14.
 */
public class RequestContainer {

	public static RequestContainer instance = new RequestContainer();

	private RequestContainer() {}

	private static ThreadLocal<InstanceContainer> threadLocal = new ThreadLocal<InstanceContainer>() {
		@Override
		protected InstanceContainer initialValue() {
			return new InstanceContainer(new HashMap<>());
		}
	};

	public void setObject(Object object, Class typeAs) {
		threadLocal.get().set(object, typeAs);
	}

	public void setObject(Object object) {
		threadLocal.get().set(object);
	}

	public <T> T getByType(Class<T> type) {
		return threadLocal.get().getInstanceByType(type);
	}

	public void clear() {
		threadLocal.remove();
	}
}
