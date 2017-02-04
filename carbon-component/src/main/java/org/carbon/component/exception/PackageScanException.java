package org.carbon.component.exception;

/**
 * @author ubuntu 2017/01/14.
 */
public class PackageScanException extends Exception {
    public PackageScanException(String protocol, Throwable cause) {
        super("Fail to scan files. Using protocol is ["+protocol+"]", cause);
    }
}
