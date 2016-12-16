package org.carbon.util;

/**
 * @author Shota Oda 2016/10/08.
 */
public class SimpleKeyValue {
	private String key;
	private Object value;

	public SimpleKeyValue(String key, Object value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public Object getValue() {
		return value;
	}
}
