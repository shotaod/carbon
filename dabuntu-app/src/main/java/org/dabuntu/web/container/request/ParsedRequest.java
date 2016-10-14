package org.dabuntu.web.container.request;

import org.dabuntu.web.def.HttpMethod;

/**
 * @author ubuntu 2016/10/12.
 */
public class ParsedRequest {
	private HttpMethod method;
	private MappedCookie cookie;
	private MappedRequestBody request;

	public static ParsedRequest forGet(MappedCookie cookie) {
		return new ParsedRequest(HttpMethod.GET.getCode(), cookie, null);
	}

	public ParsedRequest(String hMethod, MappedCookie cookie, MappedRequestBody request) {
		this.method = HttpMethod.codeOf(hMethod);
		this.cookie = cookie;
		this.request = request;
	}

	public HttpMethod getMethod() {
		return method;
	}

	public MappedCookie getMappedCookie() {
		return cookie;
	}

	public MappedRequestBody getMappedRequestBody() {
		return request;
	}
}
