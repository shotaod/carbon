package org.dabuntu.web.auth;

import org.dabuntu.web.context.SessionContainer;

import java.util.Optional;

/**
 * @author ubuntu 2016/11/03.
 */
public interface AuthSessionManager<IDENTITY extends AuthIdentity> {
	Optional<IDENTITY> get(SessionContainer session);
	void set(IDENTITY identity, SessionContainer session);
	void remove(SessionContainer session);
}
