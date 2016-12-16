package org.carbon.component.generator;

import java.util.function.Function;

/**
 * @author Shota Oda 2016/11/28.
 */
@FunctionalInterface
public interface CallbackCondition extends Function<Class, Boolean> {
}
