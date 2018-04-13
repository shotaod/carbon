package org.carbon.web.context.session.key;

/**
 * @author Shota.Oda 2018/03/03.
 */
public interface SessionKey {
    String key();

    boolean expired();
}
