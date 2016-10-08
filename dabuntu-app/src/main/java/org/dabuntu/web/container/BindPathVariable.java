package org.dabuntu.web.container;

/**
 * @author ubuntu 2016/10/08.
 */
public class BindPathVariable {
	private String pathVariableMark;
	private Class argumentType;
	private String argumentName;

	public BindPathVariable(Class argumentType, String argumentName, String pathVariableMark) {
		this.argumentType = argumentType;
		this.argumentName = argumentName;
		this.pathVariableMark = pathVariableMark;
	}

	public Class getArgumentType() {
		return argumentType;
	}
	public String getArgumentName() {
		return argumentName;
	}
	public String getPathVariableMark() {
		return pathVariableMark;
	}
}
