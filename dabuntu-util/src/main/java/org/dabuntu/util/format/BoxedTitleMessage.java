package org.dabuntu.util.format;

import org.dabuntu.util.SimpleKeyValue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ubuntu 2016/10/08.
 */
public class BoxedTitleMessage {
	private static final String BoxPrefix = "[";
	private static final String BoxSuffix = "]";

	private abstract static class JoinType {
		abstract Collector<CharSequence, ?, String> get(String title);

		private static _Left Left() {
			return new _Left();
		}

		private static _Right Right() {
			return new _Right();
		}

		private static class _Left extends JoinType {
			@Override
			public Collector<CharSequence, ?, String> get(String title) {
				return Collectors.joining("", BoxPrefix + title, BoxSuffix);
			}
		}

		private static class _Right extends JoinType {
			@Override
			public Collector<CharSequence, ?, String> get(String title) {
				return Collectors.joining("", BoxPrefix, title + BoxSuffix);
			}
		}
	}

	public static String produceRight(List<SimpleKeyValue> keyValues) {
		return create(keyValues, JoinType.Right());
	}

	public static String produceLeft(List<SimpleKeyValue> keyValues) {
		return create(keyValues, JoinType.Left());
	}

	private static String create(List<SimpleKeyValue> keyValues, JoinType type) {
		Integer maxLen = keyValues.stream().map(kv -> kv.getKey().length()).max(Integer::compare).get();

		return keyValues.stream()
				.map(kv -> {
					String boxedTitle = Stream.generate(() -> " ").limit(maxLen - kv.getKey().length()).collect(type.get(kv.getKey()));
					String message = kv.getValue().toString();
					if (message.contains("\n")) {
						String emptyBox = Stream.generate(() -> " ").limit(maxLen + 1).collect(Collectors.joining()) + "|" + " ";
						message = Arrays.stream(message.split("\\n")).map(s -> emptyBox + s).collect(Collectors.joining("\n"));
						message = message.substring(emptyBox.length(), message.length());
					}
					return boxedTitle + " " + message;
				}).collect(Collectors.joining("\n"));
	}
}