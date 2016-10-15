package org.dabuntu.component.generator;

import com.google.common.base.Objects;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Generate class
 * Concern about to set aop or not
 * No concern about what class generate and what class do not generate
 * @author ubuntu 2016/10/07.
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

		@Override
		public boolean equals(Object obj) {
			return this.o.equals(obj);
		}
	}

	private List<Callback> callbacks;

	public Map<Class, Object> generate(Set<Class> classes) {
		return classes.stream()
				.filter(clazz -> {
					return !clazz.isInterface() && !clazz.isAnnotation();
				})
				.flatMap(clazz -> {
					// if No callback, generate by Reflection
					if (this.callbacks == null || this.callbacks.isEmpty()) {
						try {
							return genByReflection(clazz).stream();
						} catch (InstantiationException | IllegalAccessException e) {
							e.printStackTrace();
						}
					}

					// if exist callback, generate by cglib
					Enhancer enhancer = new Enhancer();
					enhancer.setSuperclass(clazz);
					enhancer.setCallbacks(this.callbacks.toArray(new Callback[this.callbacks.size()]));
					Object o = enhancer.create();
					return Collections.singleton(new ClassAndObject(clazz, o)).stream();
				})
				.collect(Collectors.toMap(
					co -> co.getC(),
					co -> co.getO()
				));
	}

	public void setCallbacks(List<Callback> callbacks) {
		this.callbacks = callbacks;
	}

	private Set<ClassAndObject> genByReflection(Class type) throws IllegalAccessException, InstantiationException {
		if (type.isAnnotationPresent(Configuration.class)) {
			Object o = type.newInstance();
			return Arrays.stream(type.getDeclaredMethods())
				.filter(method -> method.isAnnotationPresent(Component.class))
				.map(method -> {
					try {
						Object result = method.invoke(o);
						return new ClassAndObject(method.getReturnType(), result);
					} catch (IllegalAccessException | InvocationTargetException e) {
						throw new RuntimeException(e);
					}
				})
				.collect(Collectors.toSet());
		}

		return Collections.singleton(new ClassAndObject(type, type.newInstance()));
	}
}
