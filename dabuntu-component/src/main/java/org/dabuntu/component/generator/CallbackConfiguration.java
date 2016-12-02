package org.dabuntu.component.generator;

import net.sf.cglib.proxy.Callback;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ubuntu 2016/11/25.
 */
public class CallbackConfiguration {

	public static class CallbackContainer {
		private CallbackContainer(){}
		private CallbackCondition condition;
		private Class<? extends Callback> callbackType;

		public static CallbackContainer Any(Class<? extends Callback> callbackType) {
			CallbackContainer container = new CallbackContainer();
			container.condition = type -> true;
			container.callbackType = callbackType;
			return container;
		}

		public static CallbackContainer Specify(Class<? extends Callback> callbackType, CallbackCondition condition) {
			CallbackContainer container = new CallbackContainer();
			container.condition = condition;
			container.callbackType = callbackType;
			return container;
		}

	}

	private List<CallbackContainer> callbackContainers;

	public CallbackConfiguration() {
		callbackContainers = new ArrayList<>();
	}

	/**
	 * set callback for any target
	 * @param callbackType
	 */
	public void setCallbacks(Class<? extends Callback> callbackType) {
		callbackContainers.add(CallbackContainer.Any(callbackType));
	}

	/**
	 * set callback with apply condition
	 * @param callback
	 * @param condition
	 */
	public void setCallbacks(Class<? extends Callback> callback, CallbackCondition condition) {
		callbackContainers.add(CallbackContainer.Specify(callback, condition));
	}

	public List<Class<? extends Callback>> getCallback(Class target) {
		return callbackContainers.stream()
			.filter(container -> container.condition.apply(target))
			.map(container -> container.callbackType)
			.collect(Collectors.toList());
	}
}
