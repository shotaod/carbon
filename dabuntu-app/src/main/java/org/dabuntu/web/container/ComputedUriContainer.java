package org.dabuntu.web.container;


import java.util.List;

/**
 * @author ubuntu 2016/10/08.
 */
public class ComputedUriContainer {
	private String computedUri;
	private List<BindPathVariable> variables;

	public ComputedUriContainer(String computedUri, List<BindPathVariable> variables) {
		this.computedUri = computedUri;
		this.variables = variables;
	}

	public String getComputedUri() {
		return computedUri;
	}

	public List<BindPathVariable> getVariables() {
		return variables;
	}
}
