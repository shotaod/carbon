package org.carbon.component.meta;

import java.util.Optional;

import org.apache.commons.lang3.ClassUtils;
import org.carbon.component.Switcher;
import org.carbon.component.annotation.Switch;
import org.carbon.component.exception.ClassNotRegisteredException;

/**
 * @author Shota Oda 2018/01/01.
 */
public final class ComponentMeta<T> {
    private Class<T> asClass;
    private T instance;

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
    }

    public boolean isQualified(ComponentMetaSet candidates) {
        Switch switchAnnotation = getType().getDeclaredAnnotation(Switch.class);
        if (switchAnnotation == null) return true;
        Class<? extends Switcher> switcherClass = switchAnnotation.value();
        ComponentMeta<? extends Switcher> switcherMeta = candidates.get(switcherClass);
        if (switcherMeta == null) throw new ClassNotRegisteredException(String.format("Class[%s] is required Switcher[%s], but not found", asClass, switcherClass));
        return switcherMeta.getInstance().on(getInstance().getClass());
    }

    // ===================================================================================
    //                                                                          Base
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
