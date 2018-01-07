package org.carbon.modular.exception;

/**
 * @author Shota Oda 2017/03/04.
 */
public class ModuleConfigureException extends RuntimeException {
    public ModuleConfigureException(String message) {
        super(message);
    }
    public ModuleConfigureException(String message, Throwable cause) {
        super(message, cause);
    }
}
