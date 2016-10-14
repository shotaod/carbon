package org.dabuntu.web.container.request;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ubuntu 2016/10/12.
 */
public class MappedCookie {
	private Map<String , String> cookies;

	public static MappedCookie empty() {
		return new MappedCookie(null);
	}

	public MappedCookie(Map<String, String> cookies) {
		this.cookies = cookies;
	}

	public Map<String, String> getCookies() {
		return cookies;
	}
	public String getValue(String key) {
		return cookies.get(key);
	}

	public List<String> getKeys() {
		return cookies.keySet().stream().collect(Collectors.toList());
	}

	public boolean isExist() {
		return this.cookies != null && this.cookies.size() > 0;
	}
}
