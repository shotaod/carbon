package org.carbon.web.container;

import org.carbon.web.auth.Authentication;

import java.lang.reflect.Method;

/**
 * @author ubuntu 2016/10/07.
 */
public class DefinedAction {
	private ComputedUriVariableContainer computedUriVariableContainer;
	private Authentication auth;
	private Class controllerClass;
	private Method actionMethod;

	public DefinedAction(ComputedUriVariableContainer computedUriVariableContainer,
						 Authentication auth,
						 Class controllerClass,
						 Method actionMethod) {
		this.computedUriVariableContainer = computedUriVariableContainer;
		this.auth = auth;
		this.controllerClass = controllerClass;
		this.actionMethod = actionMethod;
	}

	public ComputedUriVariableContainer getComputedUriVariableContainer() {
		return computedUriVariableContainer;
	}

	public Authentication getAuth() {
		return auth;
	}

	public Class getControllerClass() {
		return controllerClass;
	}

	public Method getActionMethod() {
		return actionMethod;
	}
}
