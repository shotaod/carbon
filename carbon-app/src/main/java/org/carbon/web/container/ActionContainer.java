package org.carbon.web.container;

import org.carbon.web.auth.Authentication;

import java.util.List;

/**
 * @author ubuntu 2016/10/05.
 */
public class ActionContainer {
	private Authentication auth;
	private ControllerAction controllerAction;
	private List<ResolvedArgument> resolvedArguments;

	public ActionContainer(Authentication auth, ControllerAction controllerAction, List<ResolvedArgument> resolvedArguments) {
		this.auth = auth;
		this.controllerAction = controllerAction;
		this.resolvedArguments = resolvedArguments;
	}

	public Authentication getAuth() {
		return auth;
	}

	public ControllerAction getControllerAction() {
		return controllerAction;
	}

	public List<ResolvedArgument> getResolvedArguments() {
		return resolvedArguments;
	}
}
