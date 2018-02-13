package org.carbon.authentication;

import java.util.Optional;

/**
 * @author Shota Oda 2016/11/03.
 */
public interface AuthIdentifier<IDENTITY extends AuthIdentity> {
    Optional<IDENTITY> find(String identity);
}
