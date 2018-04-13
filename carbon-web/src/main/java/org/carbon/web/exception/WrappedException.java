package org.carbon.web.exception;

import org.carbon.util.Describable;

/**
 * Exception Wrapper for Check exception to be as Runtime Exception
 *
 * @author Shota.Oda 2018/02/24.
 */
public abstract class WrappedException extends RuntimeException {
    private static class Wrap implements Describable {
        private Throwable throwable;

        public Wrap(Throwable throwable) {
            this.throwable = throwable;
        }

        @Override
        public String describe() {
            return String.format("wrapped exception <[%s]>", throwable.getClass().getSimpleName());
        }
    }

    private static class WrappedExceptionImpl extends WrappedException {
        public WrappedExceptionImpl(Wrap wrap) {
            super(wrap);
        }

        @Override
        protected WrappedException self() {
            return null;
        }
    }

    private Wrap wrap;

    /**
     * for client use
     *
     * @param throwable any
     * @return wrapped exception
     */
    public static WrappedException wrap(Throwable throwable) {
        if (throwable instanceof WrappedException) {
            throw new IllegalArgumentException(throwable.getClass() + "is already wrapped");
        }
        return new WrappedExceptionImpl(new Wrap(throwable));
    }

    private WrappedException(Wrap wrap) {
        super(wrap.describe());
        this.wrap = wrap;
    }

    private void initDescend() {
        this.wrap = new Wrap(self());
    }

    public final Throwable get() {
        return wrap.throwable;
    }

    // -----------------------------------------------------
    //                                               for subclassing
    //                                               -------
    protected WrappedException() {
        initDescend();
    }

    protected WrappedException(Throwable cause) {
        super(cause);
        initDescend();
    }

    protected WrappedException(String message) {
        super(message);
        initDescend();
    }

    protected WrappedException(String message, Throwable cause) {
        super(message, cause);
        initDescend();
    }

    protected abstract WrappedException self();
}
