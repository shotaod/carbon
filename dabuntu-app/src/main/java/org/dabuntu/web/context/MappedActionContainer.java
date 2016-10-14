package org.dabuntu.web.context;

import org.dabuntu.web.container.DefinedAction;
import org.dabuntu.web.def.HttpMethod;

import java.util.List;
import java.util.Map;

/**
 * @author ubuntu 2016/10/07.
 */
public class MappedActionContainer {
	Map<HttpMethod, List<DefinedAction>> container;

	public MappedActionContainer(Map<HttpMethod, List<DefinedAction>> container) {
		this.container = container;
	}

	public Map<HttpMethod, List<DefinedAction>> getContainer() {
		return container;
	}
}
