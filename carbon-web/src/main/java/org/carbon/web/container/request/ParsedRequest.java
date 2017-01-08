package org.carbon.web.container.request;

import org.carbon.web.def.HttpMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Shota Oda 2016/10/12.
 */
public class ParsedRequest {
	private HttpMethod method;
	private MappedCookie cookie;
	private HttpServletRequest rewRequest;

	public static ParsedRequest forGet(MappedCookie cookie) {
		return new ParsedRequest(HttpMethod.GET.getCode(), cookie, null);
	}

	public ParsedRequest(String hMethod, MappedCookie cookie, HttpServletRequest rewRequest) {
		this.method = HttpMethod.codeOf(hMethod);
		this.cookie = cookie;
		this.rewRequest = rewRequest;
	}

	public HttpMethod getMethod() {
		return method;
	}

	public MappedCookie getMappedCookie() {
		return cookie;
	}

	public HttpServletRequest getRewRequest() {
		return rewRequest;
	}
}
