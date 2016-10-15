package org.dabuntu.web.def;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Configuration;
import org.dabuntu.web.annotation.Controller;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ubuntu 2016/10/07.
 */
public class FactoryAcceptAnnotations {
	public static Set<Class> basic() {
		return Stream.of(
				Controller.class,
				Component.class,
				Configuration.class
		).collect(Collectors.toSet());
	}
}
