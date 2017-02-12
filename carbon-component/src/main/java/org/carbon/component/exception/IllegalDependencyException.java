package org.carbon.component.exception;

/**
 * @author Shota Oda 2017/02/11.
 */
public class IllegalDependencyException extends RuntimeException {
    public IllegalDependencyException(String message) {
        super(message);
    }
}
