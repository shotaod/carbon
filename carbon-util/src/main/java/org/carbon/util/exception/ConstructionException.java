package org.carbon.util.exception;

/**
 * @author Shota Oda 2017/03/20.
 */
public class ConstructionException extends Exception {
    public ConstructionException(Class target, Throwable cause) {
        super("Fail to instantiate class[" + target.getCanonicalName() + "]", cause);
    }
}
