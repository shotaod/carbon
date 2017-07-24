package org.carbon.util.mapper;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.carbon.util.exception.ConstructionException;
import org.carbon.util.exception.ObjectMappingException;
import org.carbon.util.exception.UndefinedValueException;
import org.carbon.util.mapper.cast.StringAnyCaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.carbon.util.mapper.cast.StringAnyCaster.boolCaster;
import static org.carbon.util.mapper.cast.StringAnyCaster.byteCaster;
import static org.carbon.util.mapper.cast.StringAnyCaster.charCaster;
import static org.carbon.util.mapper.cast.StringAnyCaster.doubleCaster;
import static org.carbon.util.mapper.cast.StringAnyCaster.floatCaster;
import static org.carbon.util.mapper.cast.StringAnyCaster.identity;
import static org.carbon.util.mapper.cast.StringAnyCaster.intCaster;
import static org.carbon.util.mapper.cast.StringAnyCaster.localDateCaster;
import static org.carbon.util.mapper.cast.StringAnyCaster.localDateTimeCaster;
import static org.carbon.util.mapper.cast.StringAnyCaster.longCaster;
import static org.carbon.util.mapper.cast.StringAnyCaster.shortCaster;

/**
 * =======================
 * check
 * defined key or not
 * (not)
 * - iterate field
 * (key defined)
 * - same
 * - list
 * - handlable
 * - pojo
 * =======================
 * @author Shota Oda 2016/10/12.
 */
public class KeyValueMapper {
    private Logger logger = LoggerFactory.getLogger(KeyValueMapper.class);
    protected class CasterStrategy<T> {
        private Class<T> type;
        private StringAnyCaster<T> caster;

        public CasterStrategy(Class<T> type, StringAnyCaster<T> caster) {
            this.type = type;
            this.caster = caster;
        }

        public boolean equalType(Class<?> other) {
            return type.equals(other);
        }

        public T cast(Object source) {
            return caster.cast(source.toString());
        }
    }

    protected boolean isDismissUndefined;
    protected List<CasterStrategy<?>> rawTypeStrategies;

    public KeyValueMapper() {
        this(true);
    }
    public KeyValueMapper(boolean isDismissUndefined) {
        this.isDismissUndefined = isDismissUndefined;
        this.rawTypeStrategies = defaultCasters();
    }

    public <T> T mapPrimitive(String source, Class<T> as) {
        return findStrategy(as).map(strategy -> strategy.cast(source)).orElseThrow(() -> {
            String message = String.format("Cannot cast to %s", as.getClass().getName());
            return new UnsupportedOperationException(message);
        });
    }

    public <T> T mapAndConstruct(Map<String, Object> sources, Class<T> as) {
        T instance;
        try {
            instance = newInstance(as, null);
        } catch (ConstructionException e) {
            throw new ObjectMappingException(e);
        }
        map(instance, sources);
        return instance;
    }

    public void map(Object instance, Map<String, Object> sources) {
        mapPojo(instance, MapPath.root(), sources);
    }

    private <T> T mapPojo(T target, MapPath mapPath, Map<String, Object> baseSource) {
        List<PropertyDescriptor> props = getSettersHasWriterOnly(target.getClass());

        for (PropertyDescriptor prop : props) {
            Class<?> setterType = prop.getPropertyType();
            MapPath nestPath = mapPath.join(prop.getName());
            Object targetSource = searchMap(baseSource, nestPath);

            if (targetSource == null) {
                logger.debug("Skip Mapping => target[{}], path{}, source[{}]", target.getClass().getCanonicalName(), nestPath, baseSource);
                continue;
            }

            Object item;
            // check list
            if (isCollection(setterType)) {
                item = mapList(getGenericType(prop), targetSource, mapPath, target);
            } else {
                // check raw type
                Optional<? extends CasterStrategy<?>> strategy = findStrategy(setterType);
                if (strategy.isPresent()) {
                    item = strategy.get().cast(targetSource);
                }
                // assert pojo
                else {
                    try {
                        Object pojo = newInstance(setterType, target);
                        item = mapPojo(pojo, nestPath, baseSource);
                    } catch (ConstructionException ignore) {
                        item = null;
                    }
                }
            }
            invokeSetter(target, prop, item);
        }

        return target;
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> mapList(Class<T> genericType, Object source, MapPath mapPath, Object parent) {
        Class<?> parentClass = genericType.getEnclosingClass();

        if (source instanceof List) {
            return (List<T>) ((List<?>) source).stream().map(sourceElement -> {
                Class<?> sourceElementClass = sourceElement.getClass();
                if (genericType.isAssignableFrom(sourceElementClass)) {
                    return (T) sourceElement;
                }
                Optional<CasterStrategy<T>> strategy = findStrategy(genericType);
                if (strategy.isPresent()) {
                    return strategy.get().cast(sourceElement);
                }
                if (sourceElement instanceof Map) {
                    T genericPojo = null;
                    try {
                        genericPojo = newInstance(genericType, parent);
                    } catch (ConstructionException e) {
                        throw new ObjectMappingException(e);
                    }
                    return mapPojo(genericPojo, MapPath.root(), (Map) sourceElement);
                }
                return null;
            }).collect(Collectors.toList());
        }

        Object instance;
        try {
            if (parentClass != null && parentClass.equals(parent.getClass())) {
                instance = newInstance(genericType, parent);
            } else {
                instance = newInstance(genericType, null);
            }
        } catch (ConstructionException e) {
            throw new ObjectMappingException(e);
        }

        // source is not list
        if (source instanceof Map) {
            return (List<T>) Collections.singletonList(mapPojo(instance, mapPath, (Map) source));
        } else if (genericType.isAssignableFrom(source.getClass())) {
            return (List<T>) Collections.singletonList(source);
        }

        throw new ObjectMappingException(String.format("Fail to map [%s] to [List<%s>]", source.getClass(), genericType));
    }

    private List<PropertyDescriptor> getSettersHasWriterOnly(Class mapTo) {
        try {
            return Arrays.stream(Introspector.getBeanInfo(mapTo).getPropertyDescriptors())
                    .filter(pd -> pd.getWriteMethod() != null)
                    .collect(Collectors.toList());
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T newInstance(Class<T> type, Object parentIfAny) throws ConstructionException {
        boolean isInnerClass = type.isMemberClass();
        if (isInnerClass) {
            try {
                if (parentIfAny == null || !parentIfAny.getClass().equals(type.getEnclosingClass())) {
                    parentIfAny = type.getEnclosingClass().newInstance();
                }
                Constructor<T> constructor = type.getDeclaredConstructor(parentIfAny.getClass());
                return constructor.newInstance(parentIfAny);
            } catch (NoSuchMethodException ignore) {
                // it is possible when type is 'static inner class'
                // try type.newInstance()
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                throw new ObjectMappingException(e);
            }
        }
        try {
            return type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ConstructionException(e);
        }
    }

    private void invokeSetter(Object instance, PropertyDescriptor prop, Object item) {
        try {
            prop.getWriteMethod().invoke(instance, item);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new ObjectMappingException(e);
        }
    }

    private Object searchMap(Map<String, Object> map, MapPath path) {
        try {
            return path.find(map);
        } catch (UndefinedValueException e) {
            if (isDismissUndefined) return null;
            else throw new ObjectMappingException(e);
        }
    }

    private Class<?> getGenericType(PropertyDescriptor prop) {
        return (Class<?>) ((ParameterizedType) prop.getReadMethod().getGenericReturnType()).getActualTypeArguments()[0];
    }

    private boolean isCollection(Class<?> type) {
        return Collection.class.isAssignableFrom(type);
    }

    @SuppressWarnings("unchecked")
    private <T> Optional<CasterStrategy<T>> findStrategy(Class<T> type) {
        for (CasterStrategy<?> strategy : rawTypeStrategies) {
            if (strategy.equalType(type)) {
                return Optional.of((CasterStrategy<T>) strategy);
            }
        }

        return Optional.empty();
    }

    private List<KeyValueMapper.CasterStrategy<?>> defaultCasters() {
        return Arrays.asList(
                new KeyValueMapper.CasterStrategy<>(String.class, identity),
                new KeyValueMapper.CasterStrategy<>(int.class, intCaster),
                new KeyValueMapper.CasterStrategy<>(Integer.class, intCaster),
                new KeyValueMapper.CasterStrategy<>(long.class, longCaster),
                new KeyValueMapper.CasterStrategy<>(Long.class, longCaster),
                new KeyValueMapper.CasterStrategy<>(float.class, floatCaster),
                new KeyValueMapper.CasterStrategy<>(Float.class, floatCaster),
                new KeyValueMapper.CasterStrategy<>(double.class, doubleCaster),
                new KeyValueMapper.CasterStrategy<>(Double.class, doubleCaster),
                new KeyValueMapper.CasterStrategy<>(byte.class, byteCaster),
                new KeyValueMapper.CasterStrategy<>(Byte.class, byteCaster),
                new KeyValueMapper.CasterStrategy<>(short.class, shortCaster),
                new KeyValueMapper.CasterStrategy<>(Short.class, shortCaster),
                new KeyValueMapper.CasterStrategy<>(boolean.class, boolCaster),
                new KeyValueMapper.CasterStrategy<>(Boolean.class, boolCaster),
                new KeyValueMapper.CasterStrategy<>(char.class, charCaster),
                new KeyValueMapper.CasterStrategy<>(Character.class, charCaster),
                new KeyValueMapper.CasterStrategy<>(LocalDate.class, localDateCaster),
                new KeyValueMapper.CasterStrategy<>(LocalDateTime.class, localDateTimeCaster)
        );
    }
}
