package org.carbon.util.mapper;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Shota Oda 2016/10/12.
 */
// TODO Shota Oda error handling refactoring
public class NameBasedObjectMapper {
	private interface Caster<T> {
		T cast(Class<T> type, String source);
	}

	private boolean dismissCastException = true;
	private Map<Class, Caster> handlableTypes;
	public NameBasedObjectMapper() {
		handlableTypes = initializeHandlable();
	}
	public NameBasedObjectMapper(boolean dismissCastException) {
		handlableTypes = initializeHandlable();
		this.dismissCastException = dismissCastException;
	}

	public void setDismissCastException(boolean dismissCastException) {
		this.dismissCastException = dismissCastException;
	}

	@SuppressWarnings("unchecked")
	public <T> T map(Map<String, Object> sources, Class<T> mapTo) {

		/* =======================
		* check
		* defined key or not
		* (not)
		* - iterate field
		* (key defined)
		* - same
		* - list
		* - handlable
		* - pojo
		======================= */

		T instance = newInstance(mapTo, null);
		LinkedList<PropertyDescriptor> props = new LinkedList<>();
		props.addAll(getSetters(mapTo));
		return (T)doMap(instance, props, sources);
	}

	@SuppressWarnings("unchecked")
	private Object doMap(Object target, LinkedList<PropertyDescriptor> setters, Map<String, Object> sources) {
		if (setters.size() == 0) {
			return target;
		}
		PropertyDescriptor prop = setters.poll();
		Class<?> propType = prop.getPropertyType();
		Object source = sources.get(prop.getName());

		if (source == null || (source instanceof String && ((String) source).trim().isEmpty())) {
			return doMap(target, setters, sources);
		}

		Class<?> sourceClass = source.getClass();

		if (propType.isAssignableFrom(sourceClass)) {
			if (List.class.isAssignableFrom(propType)) {
				Class type = (Class)((ParameterizedType) prop.getReadMethod().getGenericReturnType()).getActualTypeArguments()[0];
				Class parentClass = type.getEnclosingClass();
				Supplier<Object> elementSupplier;
				if (parentClass != null && parentClass.isAssignableFrom(target.getClass())) {
					elementSupplier = () -> newInstance(type, target);
				} else {
					elementSupplier = () -> newInstance(type, null);
				}

				if (source instanceof List) {
					List item = (List) ((List) source).stream().map(sourceElement -> {
						Class<?> sourceElementClass = sourceElement.getClass();
						if (type.isAssignableFrom(sourceElementClass)) {
							return sourceElement;
						}
						if (handlableTypes.containsKey(type)) {
							return handlableTypes.get(type).cast(type, sourceElement.toString());
						}
						if (sourceElement instanceof Map) {
							LinkedList<PropertyDescriptor> nestProps = new LinkedList<>();
							nestProps.addAll(getSetters(type));
							return doMap(elementSupplier.get(), nestProps, (Map) sourceElement);
						} else {
							return null;
						}
					}).collect(Collectors.toList());
					invokeSetter(target, prop, item);
				} else {

				}
				return doMap(target, setters, sources);
			}
			invokeSetter(target, prop, source);
			return doMap(target, setters, sources);
		}
		if (handlableTypes.containsKey(propType)) {
			Object cast = handlableTypes.get(propType).cast(propType, source.toString());
			invokeSetter(target, prop, cast);
			return doMap(target, setters, sources);
		}
		// POJO
		Object pojo = newInstance(propType, target);
		LinkedList<PropertyDescriptor> tmp = new LinkedList<>();
		tmp.addAll(getSetters(propType));
		if (Map.class.isAssignableFrom(sourceClass)) {
			invokeSetter(target, prop, doMap(pojo, tmp, (Map)source));
		}
		return doMap(target, setters, sources);
	}

	private List<PropertyDescriptor> getSetters(Class mapTo) {
		try {
			List<PropertyDescriptor> setters = Arrays.stream(Introspector.getBeanInfo(mapTo).getPropertyDescriptors())
					.filter(pd -> pd.getWriteMethod() != null)
					.collect(Collectors.toList());
			LinkedList<PropertyDescriptor> propertyDescriptors = new LinkedList<>();
			propertyDescriptors.addAll(setters);
			return propertyDescriptors;
		} catch (IntrospectionException e) {
			throw new RuntimeException();
		}
	}

	private Object findMap(String key, Map<String, Object> sources) {
		LinkedList<String> keys = new LinkedList<>();
		keys.addAll(Arrays.asList(key.split("\\.")));
		return findMap(keys, sources);
	}

	@SuppressWarnings("unchecked")
	private Object findMap(LinkedList<String> keys, Map<String, Object> sources) {
		if (keys.size() == 0) {
			String key = keys.poll();
			return sources.get(key);
		}
		String key = keys.poll();
		Map map = Optional.ofNullable(sources.get(keys.poll())).map(obj -> (Map) obj).orElse(null);
		return findMap(keys, map);
	}

	private <T> T newInstance(Class<T> type, Object parentIfAny) {
		boolean isInnerClass = type.isMemberClass();
		if (isInnerClass) {
			try {
				if (parentIfAny == null || !parentIfAny.getClass().equals(type.getEnclosingClass())) {
					parentIfAny = type.getEnclosingClass().newInstance();
				}
				Constructor<T> constructor = type.getDeclaredConstructor(parentIfAny.getClass());
				return constructor.newInstance(parentIfAny);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		try {
			return type.newInstance();
		} catch (InstantiationException e) {
			if (e.getCause() instanceof NoSuchMethodException) {

			}
			return null;
		} catch( IllegalAccessException e) {
			return null;
		}
	}

	private void invokeSetter(Object instance, PropertyDescriptor prop, Object item) {
		try {
			prop.getWriteMethod().invoke(instance, item);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private Map<Class, Caster> initializeHandlable() {
		Map<Class, Caster> map = new HashMap<>();
		Caster intCaster = (t, s) -> {
			return Integer.parseInt(s);
		};
		Caster longCaster = (t, s) -> {
			return Long.parseLong(s);
		};
		Caster floatCaster = (t, s) -> {
			return Float.parseFloat(s);
		};
		Caster doubleCaster = (t, s) -> {
			return Double.parseDouble(s);
		};
		Caster byteCaster = (t, s) -> {
			return Byte.parseByte(s);
		};
		Caster shortCaster = (t, s) -> {
			return Short.parseShort(s);
		};
		Caster boolCaster = (t, s) -> {
			return Boolean.parseBoolean(s);
		};

		map.put(int.class, intCaster);
		map.put(Integer.class, intCaster);
		map.put(long.class, longCaster);
		map.put(Long.class, longCaster);
		map.put(float.class, floatCaster);
		map.put(Float.class, floatCaster);
		map.put(double.class, doubleCaster);
		map.put(Double.class, doubleCaster);
		map.put(byte.class, byteCaster);
		map.put(Byte.class, byteCaster);
		map.put(short.class, shortCaster);
		map.put(Short.class, shortCaster);
		map.put(boolean.class, boolCaster);
		map.put(Boolean.class, boolCaster);

		map.put(char.class, (t, s) -> {
			return s.charAt(0);
		});
		map.put(Character.class, (t, s) -> {
			return s.charAt(0);
		});

		DateTimeFormatter dFormatter = DateTimeFormatter.ISO_DATE;
		DateTimeFormatter dtFormatter = DateTimeFormatter.ISO_DATE_TIME;

		map.put(LocalDate.class, (t, s) -> {
			return LocalDate.parse(s, dFormatter);
		});
		map.put(LocalDateTime.class, (t, s) -> {
			return LocalDateTime.parse(s, dtFormatter);
		});

		return map;
	}
}
