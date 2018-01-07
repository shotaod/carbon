package org.carbon.util.exception;

/**
 * @author Shota Oda 2017/02/07.
 */
public class PropertyMappingException extends RuntimeException{
    public PropertyMappingException(String message) {
        super(message);
    }
    public PropertyMappingException(Throwable cause) {
        super(cause);
    }
}
