package org.carbon.component.generator;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Configuration;
import org.carbon.component.exception.ConstructInstanceException;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Generate class
 * Concern about to construct object and set aop
 * No concern about what class generate and what class do not generate
 * @author Shota Oda 2016/10/07.
 */
public class InstanceGenerator {

	private class ClassAndObject {
		private Class c;
		private Object o;

		public ClassAndObject(Class c, Object o) {
			this.c = c;
			this.o = o;
		}

		public Class getC() {
			return c;
		}

		public Object getO() {
			return o;
		}

		@Override
		public int hashCode() {
			return this.c.hashCode();
		}

		@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
		@Override
		public boolean equals(Object obj) {
			return this.o.equals(obj);
		}
	}

	private Map<Class, Callback> tmpCallbacks = new HashMap<>();

	public Map<Class, Object> generate(Set<Class> classes, CallbackConfiguration callbackConfiguration) {
		Map<Class, Object> result = classes.stream()
			.filter(clazz -> {
				return !clazz.isInterface() && !clazz.isAnnotation();
			})
			.map(clazz -> {
				List<Class<? extends Callback>> callbackClasses = callbackConfiguration.getCallback(clazz);
				if (callbackClasses.isEmpty()) {
					try {
						return new ClassAndObject(clazz, clazz.newInstance());
					} catch (InstantiationException | IllegalAccessException e) {
						throw new ConstructInstanceException("Failed to perform Construct Instance", e);
					}
				}

				// if exist callback, generate by cglib
				Enhancer enhancer = new Enhancer();
				enhancer.setSuperclass(clazz);
				List<Callback> callbacks = callbackClasses.stream()
						.map(callbackClass -> {
							return tmpCallbacks.computeIfAbsent(callbackClass, (type) -> {
								try {
									return (Callback) type.newInstance();
								} catch (InstantiationException | IllegalAccessException e) {
									throw new ConstructInstanceException("Failed to perform Construct Instance", e);
								}
							});
						}).collect(Collectors.toList());
				enhancer.setCallbacks(callbacks.toArray(new Callback[callbacks.size()]));
				Object o = enhancer.create();
				return new ClassAndObject(clazz, o);
			})
			.collect(Collectors.toMap(
					co -> co.getC(),
					co -> co.getO()
			));

		result.putAll(tmpCallbacks);
		return result;
	}

	public Map<Class, Object> generateMethodComponent(Map<Class, Object> configurations) {
		return configurations.entrySet().stream()
			.flatMap(entry -> genByMethodComponent(entry.getKey(), entry.getValue()))
			.collect(Collectors.toMap(
				co -> co.getC(),
				co -> co.getO()
			));
	}

	private Stream<ClassAndObject> genByMethodComponent(Class type, Object object) {
		if (type.isAnnotationPresent(Configuration.class)) {
			return Arrays.stream(type.getDeclaredMethods())
				.filter(method -> method.isAnnotationPresent(Component.class))
				.map(method -> {
					try {
						Object result = method.invoke(object);
						return new ClassAndObject(method.getReturnType(), result);
					} catch (IllegalAccessException | InvocationTargetException e) {
						throw new ConstructInstanceException("Failed to perform Construct Instance", e);
					}
				});
		}

		return Stream.of(new ClassAndObject(type, object));
	}
}
