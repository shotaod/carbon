package org.dabuntu.web.container;

import java.lang.reflect.Method;

/**
 * @author ubuntu 2016/10/07.
 */
public class DefinedAction {
	private ComputedUriVariableContainer computedUriVariableContainer;
	private Class controllerClass;
	private Method actionMethod;

	public DefinedAction(ComputedUriVariableContainer computedUriVariableContainer, Class controllerClass, Method actionMethod) {
		this.computedUriVariableContainer = computedUriVariableContainer;
		this.controllerClass = controllerClass;
		this.actionMethod = actionMethod;
	}

	public ComputedUriVariableContainer getComputedUriVariableContainer() {
		return computedUriVariableContainer;
	}

	public Class getControllerClass() {
		return controllerClass;
	}

	public Method getActionMethod() {
		return actionMethod;
	}
}
