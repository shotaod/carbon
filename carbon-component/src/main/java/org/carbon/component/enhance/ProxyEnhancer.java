package org.carbon.component.enhance;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Configuration;
import org.carbon.component.exception.ConstructInstanceException;
import org.carbon.component.meta.ComponentMeta;
import org.carbon.component.meta.ComponentMetaSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Constructor for Class and enhance by proxy
 *
 * @author Shota Oda 2016/10/07.
 */
public class ProxyEnhancer {
    private static Logger logger = LoggerFactory.getLogger(ProxyEnhancer.class);

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
                .collect(java.util.stream.Collectors.toList());

        Map<Class<? extends ProxyAdapter>, ProxyAdapter> adapters = proxyAdapters.stream().collect(java.util.stream.Collectors.toMap(
                ProxyAdapter::getClass,
                Function.identity()
        ));

        Map<Class, Object> generateInstances = classes.stream()
                .filter(clazz -> !clazz.isInterface()
                        && !clazz.isAnnotation()
                        && !adapters.keySet().contains(clazz))
                .map(clazz -> {
                    if (proxyAdapters.isEmpty()) {
                        return new ClassAndObject(clazz, constructClass(clazz));
                    }
                    // check target proxy
                    List<ProxyAdapter> handleProxyAdapters = proxyAdapters.stream()
                            .filter(proxyAdapter -> proxyAdapter.shouldHandle(clazz))
                            .collect(java.util.stream.Collectors.toList());
                    if (handleProxyAdapters.isEmpty()) {
                        return new ClassAndObject(clazz, constructClass(clazz));
                    }

                    if (logger.isDebugEnabled()) {
                        List<? extends Class<? extends ProxyAdapter>> proxies = handleProxyAdapters.stream()
                                .map(ProxyAdapter::getClass)
                                .collect(java.util.stream.Collectors.toList());
                        logger.debug("enhance {} by interceptors({})", clazz, proxies);
                    }

                    // if exist callback, generate by cglib
                    Enhancer enhancer = new Enhancer();
                    enhancer.setSuperclass(clazz);
                    enhancer.setCallbacks(handleProxyAdapters.toArray(new Callback[handleProxyAdapters.size()]));
                    return new ClassAndObject(clazz, enhancer.create());
                })
                .collect(java.util.stream.Collectors.toMap(
                        ClassAndObject::getC,
                        ClassAndObject::getO
                ));
        generateInstances.putAll(adapters);

        return generateInstances;
    }

    public ComponentMetaSet generateMethodComponent(ComponentMeta meta) {
        return doGenerateByMethodComponent(meta.getType(), meta.getInstance()).collect(ComponentMetaSet.Collectors.toSet());
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

    private Stream<ComponentMeta> doGenerateByMethodComponent(Class<?> holderClass, Object object) {
        if (holderClass.isAnnotationPresent(Configuration.class)) {
            return Arrays.stream(holderClass.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(Component.class))
                    .flatMap(method -> {
                        try {
                            logger.debug("[Enhance ] call method [{}] at @Configuration class[{}]", method.getName(), holderClass.getName());
                            Object result = method.invoke(object);
                            if (result == null) {
                                return Stream.empty();
                            }
                            return Stream.of(ComponentMeta.implAs(method.getReturnType(), result));
                        } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                            throw new ConstructInstanceException(String.format("Failed to perform Construct Instance by @Configuration [%s]", holderClass.getName()), e);
                        }
                    });
        }

        return Stream.empty();
    }
}
