package org.dabuntu.web.container;

import org.dabuntu.web.container.raw.RawUrlVariableBinding;

import java.util.List;

/**
 * @author ubuntu 2016/10/05.
 */
public class ActionContainer {
	private UriBindAction action;
	private List<RawUrlVariableBinding> rawBindings;

	public ActionContainer(UriBindAction action, List<RawUrlVariableBinding> rawBindings) {
		this.action = action;
		this.rawBindings = rawBindings;
	}

	public UriBindAction getAction() {
		return action;
	}

	public List<RawUrlVariableBinding> getRawBindings() {
		return rawBindings;
	}
}
