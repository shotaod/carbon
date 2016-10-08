package org.dabuntu.web.container;

import org.dabuntu.web.def.HttpMethod;

import java.util.List;
import java.util.Map;

/**
 * @author ubuntu 2016/10/07.
 */
public class MappedActionContainer {
	Map<HttpMethod, List<UriBindAction>> container;

	public MappedActionContainer(Map<HttpMethod, List<UriBindAction>> container) {
		this.container = container;
	}

	public Map<HttpMethod, List<UriBindAction>> getContainer() {
		return container;
	}
}
