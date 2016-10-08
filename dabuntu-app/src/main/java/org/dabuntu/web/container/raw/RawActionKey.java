package org.dabuntu.web.container.raw;

import org.dabuntu.web.def.HttpMethod;

/**
 * @author ubuntu 2016/10/07.
 */
public class RawActionKey {
	HttpMethod method;
	String url;

	public RawActionKey(HttpMethod method, String url) {
		this.method = method;
		this.url = url;
	}

	public HttpMethod getMethod() {
		return method;
	}

	public String getUrl() {
		return url;
	}
}
