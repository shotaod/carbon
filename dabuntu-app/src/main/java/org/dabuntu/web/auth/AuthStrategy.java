package org.dabuntu.web.auth;

import lombok.Getter;
import lombok.Setter;
import org.dabuntu.web.def.HttpMethod;

/**
 * @author ubuntu 2016/10/27.
 */
@Getter
@Setter
public abstract class AuthStrategy<IDENTITY extends AuthIdentity> {
	private Class<IDENTITY> identityType;
	private String baseUrl;
	private String redirectUrl;
	private AuthSessionManger<IDENTITY> sessionManager;
	private AuthRequestMapper requestMapper;
	private AuthResponseCreator responseCreator;
	private AuthIdentifier<IDENTITY> identifier;
	private AuthFinisher finisher;

	abstract public boolean shouldTryLogin(HttpMethod method, String requestUrl);
}
