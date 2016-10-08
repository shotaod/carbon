package org.dabuntu.component.classFactory;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ubuntu 2016/10/07.
 */
public class ClassFactory {

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
	}

	private List<Callback> callbacks;
	private List<Class> acceptAnnotations;

	public Map<Class, Object> initialize(List<Class> classes) {
		return classes.stream()
				.filter(clazz -> {
					return Arrays.stream(clazz.getDeclaredAnnotations())
						.anyMatch(annotation -> {
							if (this.acceptAnnotations == null || this.acceptAnnotations.isEmpty()) {
								return true;
							}
							boolean should = acceptAnnotations.contains(annotation.annotationType());
							return should;
						});
				})
				.map(clazz -> {
					if (this.callbacks == null || this.callbacks.isEmpty()) {
						try {
							return new ClassAndObject(clazz, clazz.newInstance());
						} catch (InstantiationException | IllegalAccessException e) {
							e.printStackTrace();
						}
					}
					Enhancer enhancer = new Enhancer();
					enhancer.setSuperclass(clazz);
					enhancer.setCallbacks(this.callbacks.toArray(new Callback[this.callbacks.size()]));
					Object o = enhancer.create();
					return new ClassAndObject(clazz, o);
				})
				.collect(Collectors.toMap(
					co -> co.getC(),
					co -> co.getO()
				));
	}

	public void setCallbacks(List<Callback> callbacks) {
		this.callbacks = callbacks;
	}
	public void setAcceptAnnotations(List<Class> acceptAnnotations) {
		this.acceptAnnotations = acceptAnnotations;
	}
}
