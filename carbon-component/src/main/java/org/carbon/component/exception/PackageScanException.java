package org.carbon.component.exception;

/**
 * @author Shota Oda 2017/01/14.
 */
public class PackageScanException extends RuntimeException {
    public PackageScanException(String protocol, Throwable cause) {
        super("Fail to scan class file. protocol[" + protocol + "] is not supported ", cause);
    }
}
