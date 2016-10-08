package org.dabuntu.web.container;

import java.lang.reflect.Method;

/**
 * @author ubuntu 2016/10/07.
 */
public class UriBindAction {
	private ComputedUriContainer uriComputedContainer;
	private Class controllerClass;
	private Method action;

	public UriBindAction(ComputedUriContainer uriComputedContainer, Class controllerClass, Method action) {
		this.uriComputedContainer = uriComputedContainer;
		this.controllerClass = controllerClass;
		this.action = action;
	}

	public ComputedUriContainer getUriComputedContainer() {
		return uriComputedContainer;
	}

	public Class getControllerClass() {
		return controllerClass;
	}

	public Method getAction() {
		return action;
	}
}
