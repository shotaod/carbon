package org.carbon.component.inject;

import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.carbon.component.annotation.Inject;
import org.carbon.component.exception.ClassNotRegisteredException;
import org.carbon.util.format.ChapterAttr;
import org.carbon.util.format.StringLineBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota Oda 2016/10/01
 */
public class DependencyInjector {

    private Logger logger = LoggerFactory.getLogger(DependencyInjector.class);

    public Map<Class, Object> inject(Map<Class, Object> singletons, Map<Class, Object> candidate) {
        Map<Class, Object> copy = new HashMap<>(singletons);
        copy.entrySet().forEach(entry -> inject(entry.getKey(), entry.getValue(), candidate));
        return copy;
    }

    public Map<Class, Object> injectEach(Map<Class, Object> singletons) {
        Map<Class, Object> copy = new HashMap<>(singletons);
        copy.entrySet().forEach(entry -> inject(entry.getKey(), entry.getValue(), singletons));
        return copy;
    }

    public Map<Class, Object> injectOnlySatisfied(Map<Class, Object> singletons, Map<Class, Object> candidate) {
        return singletons.entrySet().stream()
            .filter(entry -> isSatisfiedDependency(entry.getKey(), candidate))
            .map(entry -> {
                Class key = entry.getKey();
                Object instance = inject(key, entry.getValue(), candidate);
                return (Map.Entry<Class, Object>) new AbstractMap.SimpleEntry<>(key, instance);
            })
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue
            ));
    }

    private boolean isSatisfiedDependency(Class targetClass, Map<Class, Object> candidates) {
        return Arrays.stream(targetClass.getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(Inject.class))
            .map(Field::getType)
            .filter(clazz -> !candidates.containsKey(clazz))
            .collect(Collectors.toSet()).isEmpty();
    }

    private Object inject(Class clazz, Object object, Map<Class, Object> candidates) {
        Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .forEach(field -> {
                    try {
                        Object singleton = candidates.get(field.getType());

                        if (singleton == null) {
                            Inject injectAnnotation = field.getAnnotation(Inject.class);
                            if (injectAnnotation.optional()) return;
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

    private void throwClassNotRegisteredException(Class parent, Class child) {
        throw new ClassNotRegisteredException(String.format("class '%s' need @Inject '%s', but not registered", parent.getName(), child.getName()));
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
