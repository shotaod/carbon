package org.dabuntu.component.instanceFactory;

import org.dabuntu.component.exception.ClassNotRegisterdException;
import org.dabuntu.component.instanceFactory.annotation.Component;
import org.dabuntu.component.instanceFactory.annotation.Inject;
import org.dabuntu.util.format.StringLineBuilder;
import org.dabuntu.util.format.TagAttr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ubuntu 2016/10/01
 */
public class InstanceFactory {

	// ===================================================================================
	//                                                                          Logger
	//                                                                          ==========
	private Logger logger = LoggerFactory.getLogger(InstanceFactory.class);

	// ===================================================================================
	//                                                                          	Field
	//                                                                              ======
	private Map<Class, Object> singletons;

	public InstanceFactory(Map<Class, Object> singletons) {
		this.singletons = singletons;
	}

	public Map<Class, Object> run() throws IOException{
		this.singletons.entrySet().stream()
			.filter(entry -> entry.getKey().isAnnotationPresent(Component.class))
			.forEach(entry -> this.inject(entry.getKey(), entry.getValue()));

		showDependencies();

		return this.singletons;
	}

	private Object inject(Class clazz, Object object) {
		Arrays.stream(clazz.getDeclaredFields())
				.filter(field -> field.isAnnotationPresent(Inject.class))
				.forEach(field -> {
					try {
						Object singleton = singletons.get(field.getType());

						if (singleton == null) {
							throwClassNotRegisteredException(field.getType());
						}

						field.setAccessible(true);
						field.set(object, singleton);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				});
		return object;
	}

	private void throwClassNotRegisteredException(Class clazz) {
		throw new ClassNotRegisterdException(" " + clazz.getName() + "is not registered");
	}

	// ===================================================================================
	//                                                                          Logging
	//                                                                          ==========
	private void showDependencies() {
		StringLineBuilder sb = TagAttr.getBuilder("Injection Result");
		this.singletons.forEach((k,v) -> {
			this.showDependencies(sb, k, 0);
		});
		logger.debug(sb.toString());
	}

	public String getDependencyText() {
		StringLineBuilder sb = TagAttr.getBuilder("Injection Result");
		this.singletons.forEach((k,v) -> {
			this.showDependencies(sb, k, 0);
		});
		return sb.toString();
	}

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
