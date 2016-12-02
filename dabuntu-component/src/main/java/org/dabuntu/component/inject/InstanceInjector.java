package org.dabuntu.component.inject;

import org.dabuntu.component.annotation.Inject;
import org.dabuntu.component.exception.ClassNotRegisterdException;
import org.dabuntu.util.format.ChapterAttr;
import org.dabuntu.util.format.StringLineBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ubuntu 2016/10/01
 */
public class InstanceInjector {

	// ===================================================================================
	//                                                                          Logger
	//                                                                          ==========
	private Logger logger = LoggerFactory.getLogger(InstanceInjector.class);

	// ===================================================================================
	//                                                                          	Field
	//                                                                              ======
	private List<Class> targets;

	public InstanceInjector() {
	}

	public Map<Class, Object> inject(Map<Class, Object> singletons, Map<Class, Object> candidate) throws IOException {
		Map<Class, Object> copy = new HashMap<>(singletons);
		copy.entrySet().forEach(entry -> inject(entry.getKey(), entry.getValue(), candidate));
		return copy;
	}

	public Map<Class, Object> inject(Map<Class, Object> singletons) throws IOException {
		Map<Class, Object> copy = new HashMap<>(singletons);
		copy.entrySet().forEach(entry -> inject(entry.getKey(), entry.getValue(), singletons));

		return copy;
	}

	public void showDependencies(Map<Class, Object> instances) {
		StringLineBuilder sb = ChapterAttr.getBuilder("Injection Result");
		instances.entrySet().stream()
				.sorted((e1, e2) -> e1.getKey().getName().toLowerCase().compareTo(e2.getKey().getName().toLowerCase()))
				.collect(Collectors.toMap(
						e -> e.getKey(),
						e -> e.getValue(),
						(can1, can2) -> can1,
						LinkedHashMap::new))
				.forEach((k, v) -> {
					this.showDependencies(sb, k, 0);
				});
		logger.debug(sb.toString());
	}

	// ===================================================================================
	//                                                                          Core Function
	//                                                                          ==========
	private Object inject(Class clazz, Object object, Map<Class, Object> candidates) {
		Arrays.stream(clazz.getDeclaredFields())
				.filter(field -> field.isAnnotationPresent(Inject.class))
				.forEach(field -> {
					try {
						Object singleton = candidates.get(field.getType());

						if (singleton == null) {
							throwClassNotRegisteredException(clazz, field.getType());
						}

						field.setAccessible(true);
						field.set(object, singleton);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				});
		return object;
	}

	private void throwClassNotRegisteredException(Class parent, Class child) {
		throw new ClassNotRegisterdException(String.format("class '%s' need @Inject '%s', but not registered", parent.getName(), child.getName()));
	}

	// ===================================================================================
	//                                                                          Logging
	//                                                                          ==========

	private StringLineBuilder showDependencies(StringLineBuilder sb, Class clazz, Integer depth) {
		String space = Stream.generate(() -> "    ").limit(depth).collect(Collectors.joining());
		if (depth > 0) {
			space += (" <- ");
		}
		sb.appendLine(space + clazz.getName());
		Arrays.stream(clazz.getDeclaredFields())
				.filter(field -> field.isAnnotationPresent(Inject.class))
				.forEach(field -> {
					showDependencies(sb, field.getType(), depth + 1);
				});
		return sb;
	}
}
