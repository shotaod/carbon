package org.dabuntu.util.mapper;

import org.dabuntu.util.exception.ObjectMappingException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * @author ubuntu 2016/10/12.
 */
public class NameBasedObjectMapper {
	private boolean dismissCastException = true;

	public NameBasedObjectMapper() {
	}
	public NameBasedObjectMapper(boolean dismissCastException) {
		this.dismissCastException = dismissCastException;
	}

	public void setDismissCastException(boolean dismissCastException) {
		this.dismissCastException = dismissCastException;
	}

	public <T> T map(Map<String, Object> source, Class<T> mapTo) {
		T target;
		try {
			target = mapTo.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			String className = mapTo.getName();
			if (e.getCause() instanceof NoSuchMethodException) {
				throw new ObjectMappingException(String.format("Failed to construct class['%s'] Plz define 'No arg constructor in %s'", className, className), e);
			}
			throw new ObjectMappingException(String.format("Failed to construct class ['%s']", className), e);
		}

		Arrays.stream(mapTo.getDeclaredFields())
			.forEach(field -> {
				try {
					Object o = findByName(field, source);
					if (!Objects.isNull(o)) {
						field.setAccessible(true);
						field.set(target, o);
					}
				} catch (ClassCastException ignorable) {
					if (!this.dismissCastException) {
						throw new ObjectMappingException("Failed to set field", ignorable);
					}
				} catch (IllegalAccessException impossible) {
					impossible.printStackTrace();
				}
			});

		return target;
	}

	private Object findByName(Field field, Map<String, Object> source) {
		Object o = source.get(field.getName());
		return field.getType().cast(o);
	}

}
