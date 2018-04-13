package org.carbon.component.exception;

/**
 * @author Shota Oda 2016/10/02
 */
public class ClassNotRegisteredException extends Exception {
    public ClassNotRegisteredException(Class emptyClass) {
        super(emptyClass.getName() + " is not registered");
    }

    public ClassNotRegisteredException(String message) {
        super(message);
    }
}
