package org.carbon.authentication;

/**
 * @author Shota Oda 2016/11/03.
 */
public interface AuthIdentity {
    String identity();
    String cryptSecret();
    boolean confirm(String plainPassword);
}
