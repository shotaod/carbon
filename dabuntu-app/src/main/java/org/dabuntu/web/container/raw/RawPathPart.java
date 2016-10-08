package org.dabuntu.web.container.raw;

/**
 * @author ubuntu 2016/10/07.
 */
public class RawPathPart {
	private static final String prefix = "{";
	private static final String suffix = "}";

	private String part;
	private boolean pathVariable;

	public RawPathPart(String part) {
		this.pathVariable = part.startsWith(prefix) && part.endsWith(suffix);
		this.part = part.replace("{","").replace("}","");
	}

	public String getPart() {
		return this.part;
	}

	public boolean isPathVariable() {
		return this.pathVariable;
	}
}
