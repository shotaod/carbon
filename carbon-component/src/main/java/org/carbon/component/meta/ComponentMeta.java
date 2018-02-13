package org.carbon.component.meta;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Optional;

import org.apache.commons.lang3.ClassUtils;
import org.carbon.component.exception.ClassNotRegisteredException;
import org.carbon.component.exception.ImpossibleDetermineException;

/**
 * @author Shota Oda 2018/01/01.
 */
public final class ComponentMeta<T> {
    private Class<T> asClass;
    private T instance;
    private ComponentMetaSet parent;

    public static <T> ComponentMeta<T> noImpl(Class<T> asClass) {
        return new ComponentMeta<>(asClass, null);
    }

    @SuppressWarnings("unchecked")
    public static <T> ComponentMeta<T> impl(T instance) {
        return new ComponentMeta<>((Class<T>) instance.getClass(), instance);
    }

    @SuppressWarnings("unchecked")
    public static ComponentMeta implAs(Class<?> asClass, Object instance) {
        if (!ClassUtils.isAssignable(instance.getClass(), asClass)) {
            throw new IllegalArgumentException("instance(type["+instance.getClass()+"]) cannot not assign to asClass["+asClass+"]");
        }
        return new ComponentMeta(asClass, instance);
    }

    private ComponentMeta(Class<T> asClass, T instance) {
        this.asClass = asClass;
        this.instance = instance;
    }

    public Class<T> getType() {
        return asClass;
    }

    public T getInstance() {
        return instance;
    }

    public boolean hasNoImpl() {
        return instance == null;
    }

    public void setParent(ComponentMetaSet parent) {
        this.parent = parent;
    }

    @SuppressWarnings("unchecked")
    public void merge(ComponentMeta otherMeta) {
        if (!asClass.equals(otherMeta.asClass)) {
            return;
        }
        if (instance != null && otherMeta.instance != null) {
            throw new IllegalStateException("Component(as-type[" + asClass + "]) has multiple candidate instance\n-" + instance.getClass() + "\n-" + otherMeta.instance.getClass());
        }
        if (otherMeta.instance != null) {
            instance = (T) otherMeta.instance;
        }
        if (otherMeta.parent != null) {
            this.parent = otherMeta.parent;
        }
    }

    public boolean annotatedBy(Class<? extends Annotation> annotation) {
        return instance.getClass().isAnnotationPresent(annotation) || asClass.isAnnotationPresent(annotation);
    }

    public <A extends Annotation> A getAnnotate(Class<A> annotation) {
        A instanceClassAnnotate = instance.getClass().getDeclaredAnnotation(annotation);
        if (instanceClassAnnotate == null) return asClass.getDeclaredAnnotation(annotation);
        return instanceClassAnnotate;
    }

    public Field[] getPrivateField() {
        return instance.getClass().getDeclaredFields();
    }

    // ===================================================================================
    //                                                                   ComponentMeta Ext
    //                                                                          ==========
    public boolean isQualified() throws ImpossibleDetermineException {
        return parent.isQualified(this);
    }

    public void awareDependency(ComponentMetaSet dependency) throws ClassNotRegisteredException {
        parent.awareDependency(this, dependency);
    }

    // ===================================================================================
    //                                                                                Base
    //                                                                          ==========

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComponentMeta)) return false;

        ComponentMeta<?> that = (ComponentMeta<?>) o;

        return asClass.equals(that.asClass);
    }

    @Override
    public int hashCode() {
        return asClass.hashCode();
    }

    @Override
    public String toString() {
        return "asClass: " + asClass.getName() + " instance: " + Optional.ofNullable(instance).map(Object::toString).orElse("null");
    }
}
