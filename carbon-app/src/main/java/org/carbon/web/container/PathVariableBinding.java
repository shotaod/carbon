package org.carbon.web.container;

/**
 * org.dabuntu.web.annotation.@PathVariableで定義されたパス変数を格納する
 * @author ubuntu 2016/10/08.
 */
public class PathVariableBinding {
	private String pathVariableMark;
	private Class argumentType;
	private String argumentName;

	public PathVariableBinding(Class argumentType, String argumentName, String pathVariableMark) {
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
