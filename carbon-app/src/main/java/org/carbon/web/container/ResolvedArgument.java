package org.carbon.web.container;

import java.lang.reflect.Parameter;

/**
 * @author Shota Oda 2016/10/08.
 */
public class ResolvedArgument {
	// argument name correspond to value
	private String argName;
	// extracted value from request or context
	private Object value;

	public ResolvedArgument(PathVariableBinding bpv, String value) {
		this.argName = bpv.getArgumentName();
		this.value = value;
	}

	public ResolvedArgument(String argName, Object value) {
		this.argName = argName;
		this.value = value;
	}

	public String getArgName() {
		return argName;
	}
	public Object getValue() {
		return value;
	}

	public boolean equals(Parameter parameter) {
		if (parameter == null) return false;

		return parameter.getType().equals(this.value.getClass()) && parameter.getName().equals(this.argName);
	}
}
