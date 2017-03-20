package org.carbon.component.construct;

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
import org.carbon.component.construct.proxy.ProxyAdapter;
import org.carbon.component.exception.ConstructInstanceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Constructor for Class and enhance by proxy
 *
 * @author Shota Oda 2016/10/07.
 */
public class ClassConstructor {
    private static Logger logger = LoggerFactory.getLogger(ClassConstructor.class);

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

    public Map<Class, Object> generate(Set<Class> classes) {

        List<ProxyAdapter> proxyAdapters = classes.stream()
                .filter(clazz -> !clazz.isInterface() && !clazz.isAnnotation() && ProxyAdapter.class.isAssignableFrom(clazz))
                .map(clazz -> (ProxyAdapter) constructClass(clazz))
                .collect(Collectors.toList());

        return classes.stream()
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

                    if (logger.isDebugEnabled()) {
                        List<? extends Class<? extends ProxyAdapter>> proxies = handleProxyAdapters.stream().map(ProxyAdapter::getClass).collect(Collectors.toList());
                        logger.debug("enhance {} by interceptors({})", clazz, proxies);
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
    }

    public Map<Class, Object> generateMethodComponent(Map<Class, Object> configurations) {
        return configurations.entrySet().stream()
                .flatMap(entry -> doGenerateByMethodComponent(entry.getKey(), entry.getValue()))
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

    private Stream<ClassAndObject> doGenerateByMethodComponent(Class<?> holderClass, Object object) {
        if (holderClass.isAnnotationPresent(Configuration.class)) {
            return Arrays.stream(holderClass.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(Component.class))
                    .map(method -> {
                        try {
                            Object result = method.invoke(object);
                            return new ClassAndObject(method.getReturnType(), result);
                        } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                            throw new ConstructInstanceException(String.format("Failed to perform Construct Instance by @Configuration [%s]", holderClass.getName()), e);
                        }
                    });
        }

        return Stream.empty();
    }
}
