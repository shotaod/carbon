package org.carbon.component.generator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Configuration;
import org.carbon.component.exception.ConstructInstanceException;
import org.carbon.component.generator.proxy.ProxyAdapter;

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

    @SuppressWarnings("unchecked")
    public Map<Class, Object> generate(Set<Class> classes) {

        List<ProxyAdapter> proxyAdapters = classes.stream()
            .filter(clazz -> !clazz.isInterface() && !clazz.isAnnotation() && ProxyAdapter.class.isAssignableFrom(clazz))
            .map(clazz -> (ProxyAdapter) constructClass(clazz))
            .collect(Collectors.toList());

        Map<Class, Object> result = classes.stream()
            .filter(clazz -> !clazz.isInterface() && !clazz.isAnnotation())
            .map(clazz -> {
                if (proxyAdapters.isEmpty()) {
                    return new ClassAndObject(clazz, constructClass(clazz));
                }
                // check target proxy
                List<ProxyAdapter> handleProxyAdapters = proxyAdapters.stream()
                        .filter(proxyAdapter -> proxyAdapter.shouldHandle(clazz))
                        .collect(Collectors.toList());
                if (handleProxyAdapters.isEmpty()) {
                    return new ClassAndObject(clazz, constructClass(clazz));
                }

                // if exist callback, generate by cglib
                Enhancer enhancer = new Enhancer();
                enhancer.setSuperclass(clazz);
                enhancer.setCallbacks(handleProxyAdapters.toArray(new Callback[handleProxyAdapters.size()]));
                return new ClassAndObject(clazz, enhancer.create());
            })
            .collect(Collectors.toMap(
                ClassAndObject::getC,
                ClassAndObject::getO
            ));
        return result;
    }

    public Map<Class, Object> generateMethodComponent(Map<Class, Object> configurations) {
        return configurations.entrySet().stream()
            .flatMap(entry -> genByMethodComponent(entry.getKey(), entry.getValue()))
            .collect(Collectors.toMap(
                ClassAndObject::getC,
                ClassAndObject::getO
            ));
    }

    public Object constructClass(Class<?> clazz) {
        try {
            Constructor constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new ConstructInstanceException(String.format("Failed to construct Instance [%s]", clazz.getName()), e);
        }
    }

    private Stream<ClassAndObject> genByMethodComponent(Class<?> type, Object object) {
        if (type.isAnnotationPresent(Configuration.class)) {
            return Arrays.stream(type.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Component.class))
                .map(method -> {
                    try {
                        Object result = method.invoke(object);
                        return new ClassAndObject(method.getReturnType(), result);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new ConstructInstanceException(String.format("Failed to perform Construct Instance by @Configuration [%s]", type.getName()), e);
                    }
                });
        }

        return Stream.of(new ClassAndObject(type, object));
    }
}
