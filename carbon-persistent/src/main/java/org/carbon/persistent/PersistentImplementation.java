package org.carbon.persistent;

/**
 * @author Shota Oda 2016/11/26.
 */
public enum PersistentImplementation {
	Hibernate("hibernate"),
	Jooq("jooq"),
	None("none")
	;

	private String name;

	PersistentImplementation(String name) {
		this.name = name;
	}

	public static PersistentImplementation nameOf(String name) {
		for (PersistentImplementation impl : values()) {
			if (impl.name.equals(name)) {
				return impl;
			}
		}
		return None;
	}
}
