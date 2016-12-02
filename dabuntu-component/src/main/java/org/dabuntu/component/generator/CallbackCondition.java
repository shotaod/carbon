package org.dabuntu.component.generator;

import java.util.function.Function;

/**
 * @author ubuntu 2016/11/28.
 */
@FunctionalInterface
public interface CallbackCondition extends Function<Class, Boolean> {
}
