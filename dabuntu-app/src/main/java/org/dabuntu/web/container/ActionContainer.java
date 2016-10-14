package org.dabuntu.web.container;

import java.util.List;

/**
 * @author ubuntu 2016/10/05.
 */
public class ActionContainer {
	private ControllerAction controllerAction;
	private List<ResolvedArgument> resolvedArguments;

	public ActionContainer(ControllerAction controllerAction, List<ResolvedArgument> resolvedArguments) {
		this.controllerAction = controllerAction;
		this.resolvedArguments = resolvedArguments;
	}

	public ControllerAction getControllerAction() {
		return controllerAction;
	}

	public List<ResolvedArgument> getResolvedArguments() {
		return resolvedArguments;
	}
}
