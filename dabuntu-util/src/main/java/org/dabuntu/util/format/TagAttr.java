package org.dabuntu.util.format;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ubuntu 2016/10/08.
 */
public class TagAttr {
	private static final String Upper = "================================================================================";

	public static String get(String title) {
		return getBuilder(title).toString();
	}

	public static StringLineBuilder getBuilder(String title) {
		int titleLength = title.length();
		String bot = Stream.generate(() -> "=").limit(titleLength).collect(Collectors.joining());
		String middle = getSpaces(title).collect(Collectors.joining("", "", title));
		String bottom = getSpaces(title).collect(Collectors.joining("","",bot));

		StringLineBuilder sb = new StringLineBuilder();
		return sb.appendLine("").appendLine(Upper).appendLine(middle).appendLine(bottom);
	}

	private static Stream<String> getSpaces(String title) {
		return Stream.generate(() -> " ").limit(Upper.length() - title.length());
	}
}
