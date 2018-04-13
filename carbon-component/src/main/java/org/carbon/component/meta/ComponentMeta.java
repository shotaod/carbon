package org.carbon.component.meta;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang3.ClassUtils;
import org.carbon.component.exception.ClassNotRegisteredException;
import org.carbon.component.exception.IllegalDeclarationException;
import org.carbon.component.exception.ImpossibleDetermineException;
import org.carbon.util.Describable;

/**
 * @author Shota Oda 2018/01/01.
 */
public final class ComponentMeta<T> implements Describable {
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
            throw new IllegalArgumentException("instance(type[" + instance.getClass() + "]) cannot not assign to asClass[" + asClass + "]");
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


    public <A extends Annotation> A getAnnotation(Class<A> type) {
        A instanceClassAnnotate = instance.getClass().getDeclaredAnnotation(type);
        if (instanceClassAnnotate == null) return asClass.getDeclaredAnnotation(type);
        return instanceClassAnnotate;
    }


    public boolean annotatedBy(Class<? extends Annotation> annotation) {
        return instance.getClass().isAnnotationPresent(annotation) || asClass.isAnnotationPresent(annotation);
    }

    public Field[] getDeclaredField() {
        return instance.getClass().getDeclaredFields();
    }

    public boolean isAssignableTo(Type other) {
        Class<T> self = getType();
        if (other instanceof Class) {
            return ((Class<?>) other).isAssignableFrom(self);
        }

        if (other instanceof WildcardType) {
            Type[] upperBounds = ((WildcardType) other).getUpperBounds();
            if (upperBounds.length != 1) {
                throw new IllegalDeclarationException(other.getTypeName() + " is illegal, Single Wildcard(*) declaration is only supported");
            }
            Type upperType = upperBounds[0];
            if (upperType instanceof Class) {
                return ((Class<?>) upperType).isAssignableFrom(self);
            } else {
                throw new IllegalDeclarationException(other.getTypeName() + " is illegal declaration");
            }
        }

        if (other instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) other;
            Type rawType = pType.getRawType();
            if (rawType instanceof Class) {
                return ((Class<?>) rawType).isAssignableFrom(self)
                        && Stream.of(pType.getActualTypeArguments())
                        .allMatch(generic -> generic.equals(Object.class) || generic instanceof WildcardType);
            } else {
                throw new IllegalDeclarationException("Nested type is not acceptable");
            }
        }

        return false;
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

    void setParent(ComponentMetaSet parent) {
        this.parent = parent;
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
    public String describe() {
        return toString();
    }

    @Override
    public String toString() {
        return "asClass: " + asClass.getName() + " instance: " + Optional.ofNullable(instance).map(Object::toString).orElse("null");
    }
}
