package org.carbon.util.constructor;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.carbon.util.exception.ConstructionException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

/**
 * @author Shota Oda 2017/12/31.
 */
public class NoArgsConstructorUtil {
    public static boolean isInnerMemberClass(Class<?> type) {
        return type.isMemberClass() && !Modifier.isStatic(type.getModifiers());
    }

    public static <T> T construct(Class<T> type) throws ConstructionException {
        try {
            return ConstructorUtils.invokeExactConstructor(type);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new ConstructionException(type, e);
        }
    }

    public static <T> T construct(Class<T> type, Object enclosing) throws ConstructionException {
        assertInnerMemberClassArgument(type, enclosing);
        try {
            return ConstructorUtils.invokeExactConstructor(type, enclosing);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ConstructionException(type, e);
        }
    }

    private static void assertInnerMemberClassArgument(Class<?> type, Object enclosing) throws ConstructionException {
        if (!isInnerMemberClass(type)) {
            IllegalArgumentException cause = new IllegalArgumentException(type + " is not inner no-static class");
            throw new ConstructionException(type, cause);
        }
        if (enclosing == null) {
            IllegalArgumentException cause = new IllegalArgumentException("class[" + type + "] is inner(no-static) class, but Enclosing class is not provided");
            throw new ConstructionException(type, cause);
        }
        Class<?> enclosingClass = type.getEnclosingClass();
        Class<?> parentClass = enclosing.getClass();
        if (!enclosingClass.equals(parentClass)) {
            IllegalArgumentException cause = new IllegalArgumentException("class[" + type + "] is inner(no-static) class, but Provide Enclosing Object(type[" + parentClass + "]) is not equal actual Enclosing class(" + enclosingClass + ")");
            throw new ConstructionException(type, cause);
        }
    }
}
