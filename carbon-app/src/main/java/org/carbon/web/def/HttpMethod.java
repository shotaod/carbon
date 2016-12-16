package org.carbon.web.def;

/**
 * @author Shota Oda 2016/10/05.
 */
public enum  HttpMethod {
	ALL("ALL"),
	GET("GET"),
	POST("POST"),
	HEAD("HEAD"),
	PUT("PUT"),
	OPTIONS("OPTIONS"),
	DELETE("DELETE"),
	TRACE("TRACE"),
	CONNECT("CONNECT"),
	MOVE("MOVE"),
	UNKNOWN("UNKNOWN"),
	;

	private String code;
	HttpMethod(String code) {
		this.code = code;
	}
	public static HttpMethod codeOf(String code) {
		for (HttpMethod value : values()) {
			if (value.code.equals(code)) {
				return value;
			}
		}
		return UNKNOWN;
	}
	public String getCode() {
		return code;
	}
}
