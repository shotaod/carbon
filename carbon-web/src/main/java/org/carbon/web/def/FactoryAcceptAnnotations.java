package org.carbon.web.def;

import org.carbon.web.annotation.Controller;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Configuration;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Shota Oda 2016/10/07.
 */
public final class FactoryAcceptAnnotations {
	public static Set<Class<?>> basic = Stream.of(
		Controller.class,
		Component.class,
		Configuration.class
	).collect(Collectors.toSet());
}
