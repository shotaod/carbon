package org.carbon.web.auth;

import org.carbon.web.exception.UserIdentityNotFoundException;

/**
 * @author ubuntu 2016/11/03.
 */
public interface AuthIdentifier<IDENTITY extends AuthIdentity> {
	Class<IDENTITY> getType();
	IDENTITY find(String username) throws UserIdentityNotFoundException;
}
