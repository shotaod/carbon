package org.carbon.sample.heroku.util;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author Shota Oda 2017/07/31.
 */
public class OptionalStream<T>  {
    
    private T _value;
    private Throwable _throwable;
    private static OptionalStream<?> _empty = new OptionalStream<>(null);

    private OptionalStream(T _value) {
        this._value = _value;
    }

    public static <T> OptionalStream<T> empty() {
        @SuppressWarnings("unchecked")
        OptionalStream<T> empty = (OptionalStream<T>) _empty;
        return empty;
    }

    public static <T> OptionalStream<T> of(T value) {
        if (value == null) {
            return OptionalStream.empty();
        } else {
            return new OptionalStream<>(value);
        }
    }

    public boolean isPresent() {
        return _value != null;
    }

    public boolean hasThrowable() {
        return _throwable != null;
    }


    public void ifPresent(Consumer<? super T> consumer) {
        if (isPresent()) {
            consumer.accept(_value);
        }
    }

    public OptionalStream<T> filter(Predicate<? super T> predicate) {
        if (isPresent() && predicate.test(_value)) {
            return this;
        }
        return OptionalStream.empty();
    }

    public <X extends Throwable> OptionalStream<T> whenEmptyThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (!isPresent()) {
            throw exceptionSupplier.get();
        }
        return this;
    }

    public <U> OptionalStream<U> map(Function<? super T, ? extends U> mapper) {
        if (!isPresent()) {
            return OptionalStream.empty();
        }
        return OptionalStream.of(mapper.apply(_value));
    }

    public <U> OptionalStream<U> flatMap(Function<? super T, OptionalStream<U>> mapper) {
        if (!isPresent()) {
            return OptionalStream.empty();
        }
        return mapper.apply(_value);
    }

    public OptionalStream<T> peek(Consumer<? super T> consumer) {
        if (isPresent()) {
            consumer.accept(_value);
        }
        return this;
    }

    public T get() {
        return _value;
    }

    public T orElse(T other) {
        if (isPresent()) {
            return _value;
        } else {
            return other;
        }
    }

    public T orElseGet(Supplier<? extends T> other) {
        if (isPresent()) {
            return _value;
        }
        return other.get();
    }

    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (isPresent()) {
            return _value;
        }
        throw exceptionSupplier.get();
    }
}
