package org.carbon.authentication;

import java.util.Optional;

import org.carbon.web.context.session.SessionContext;

/**
 * @author Shota Oda 2016/11/03.
 */
public interface AuthSessionManager<IDENTITY extends AuthIdentity> {
    Optional<IDENTITY> get(SessionContext session);
    void set(AuthIdentity identity, SessionContext session);
    void remove(SessionContext session);
}
