package org.dabuntu.util.format;

/**
 * @author ubuntu 2016/10/08.
 */
public class StringLineBuilder {
	private StringBuilder sb;

	public StringLineBuilder() {
		this.sb = new StringBuilder();
	}

	public void append(Object o) {
		sb.append(o);
	}


	public StringLineBuilder appendLine() {
		sb.append("\n");
		return this;
	}
	public StringLineBuilder appendLine(Object o) {
		sb.append(o);
		sb.append("\n");
		return this;
	}

	@Override
	public String toString() {
		return sb.toString();
	}
}
