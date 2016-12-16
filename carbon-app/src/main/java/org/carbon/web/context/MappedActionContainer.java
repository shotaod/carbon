package org.carbon.web.context;

import org.carbon.web.def.HttpMethod;
import org.carbon.web.container.DefinedAction;

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
