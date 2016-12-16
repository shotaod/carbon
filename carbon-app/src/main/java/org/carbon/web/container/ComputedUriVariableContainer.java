package org.carbon.web.container;


import java.util.List;

/**
 * @author ubuntu 2016/10/08.
 */
public class ComputedUriVariableContainer {
	private String computedUri;
	private List<PathVariableBinding> variables;

	public ComputedUriVariableContainer(String computedUri, List<PathVariableBinding> variables) {
		this.computedUri = computedUri;
		this.variables = variables;
	}

	public String getComputedUri() {
		return computedUri;
	}

	public List<PathVariableBinding> getVariables() {
		return variables;
	}
}
