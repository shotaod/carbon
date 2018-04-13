package org.carbon.sample.v2.conf.auth;

/**
 * @author Shota.Oda 2018/03/01.
 */
public interface SecureStrings {
    String hash(String raw);

    boolean check(String raw, String secured);
}
