package org.dabuntu.web.container.raw;

import org.dabuntu.web.container.BindPathVariable;

import java.lang.reflect.Parameter;

/**
 * @author ubuntu 2016/10/08.
 */
public class RawUrlVariableBinding {
	// argument type based Method
	private Class argType;
	// argument name correspond to urlPartValue
	private String argName;
	// extracted value in request url path
	private String urlPartValue;

	public RawUrlVariableBinding(BindPathVariable bpv, String urlPartValue) {
		this.argType = bpv.getArgumentType();
		this.argName = bpv.getArgumentName();
		this.urlPartValue = urlPartValue;
	}

	public String getArgName() {
		return argName;
	}
	public String getUrlPartValue() {
		return urlPartValue;
	}

	public boolean equals(Parameter parameter) {
		if (parameter == null) return false;

		return parameter.getType().equals(this.argType) && parameter.getName().equals(this.argName);

	}
}
