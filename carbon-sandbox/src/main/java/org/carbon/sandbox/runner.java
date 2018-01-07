package org.carbon.sandbox;

import org.carbon.component.scan.TargetBaseScanner;

/**
 * @author garden 2017/09/05.
 */
public class runner {
    public static void main(String[] args) {
        TargetBaseScanner scanner = new TargetBaseScanner();
        scanner.scan();
    }
}
