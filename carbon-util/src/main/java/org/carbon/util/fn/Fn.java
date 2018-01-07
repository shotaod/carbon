package org.carbon.util.fn;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Shota Oda 2017/12/31.
 */
public interface Fn {

    @FunctionalInterface
    interface ThrowableStatement<R> {
        R eval() throws Throwable;
    }

    @FunctionalInterface
    interface ToReturnFunction<T extends Throwable, R> extends Function<T, R> {
    }

    @FunctionalInterface
    interface ToThrowableFunction<T extends Throwable, R extends Throwable> extends Function<T, R> {
    }

    class OnHoldEval<R> {
        R value;
        Throwable throwable;

        public OnHoldEval(R value, Throwable throwable) {
            this.value = value;
            this.throwable = throwable;
        }

        public R CatchReturn(ToReturnFunction<Throwable, R> toReturn) {
            if (value != null) {
                return value;
            }
            return toReturn.apply(throwable);
        }

        public <T extends Throwable> R CatchThrow(ToThrowableFunction<Throwable, T> toThrowable) throws T {
            if (value != null) {
                return value;
            }

            throw toThrowable.apply(throwable);
        }
    }

    static <R> OnHoldEval<R> Try(ThrowableStatement<R> statement) {
        try {
            return new OnHoldEval<>(statement.eval(), null);
        } catch (Throwable e) {
            return new OnHoldEval<>(null, e);
        }
    }
}
