package org.dabuntu.web.core;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.web.auth.AuthFinisher;
import org.dabuntu.web.auth.AuthIdentity;
import org.dabuntu.web.auth.AuthRequestMapper;
import org.dabuntu.web.auth.AuthSessionManger;
import org.dabuntu.web.auth.AuthStrategy;
import org.dabuntu.web.context.SecurityContainer;
import org.dabuntu.web.context.SessionContainer;
import org.dabuntu.web.def.HttpMethod;
import org.dabuntu.web.exception.UserIdentityNotFoundException;
import org.dabuntu.web.util.ResponseUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @author ubuntu 2016/10/27.
 */
@Component
public class Authenticator {

	public boolean authenticate(SecurityContainer securityContainer,
								SessionContainer session,
								HttpServletRequest request,
								HttpServletResponse response) {

		// -----------------------------------------------------
		//                                               Check where Security need or not
		//                                               -------
		if (!securityContainer.existSecurity()) {
			return true;
		}

		// -----------------------------------------------------
		//                                               Find Security Strategy
		//                                               -------
		String requestPath = Optional.ofNullable(request.getPathInfo()).orElse("");
		Optional<AuthStrategy> strategyOp = securityContainer.getStrategies().stream()
				.filter(strategy -> requestPath.startsWith(strategy.getBaseUrl()))
				.findFirst();

		if (!strategyOp.isPresent()) {
			return true;
		}

		AuthStrategy strategy = strategyOp.get();

		// -----------------------------------------------------
		//                                               Check session
		//                                               -------
		AuthSessionManger sessionManager = strategy.getSessionManager();
		Optional sessionIdentity = sessionManager.get(session);
		if (sessionIdentity.isPresent()) {
			return true;
		}

		// -----------------------------------------------------
		//                                               Check should try login or redirect
		//                                               -------
		boolean shouldTry = strategy.shouldTryLogin(HttpMethod.codeOf(request.getMethod()), requestPath);
		if (!shouldTry) {
			ResponseUtil.redirect(response, strategy.getRedirectUrl());
			return false;
		}

		// -----------------------------------------------------
		//                                               Try login
		//                                               -------
		Optional<AuthRequestMapper.AuthInfo> authInfOp = strategy.getRequestMapper().map(request);
		if (!authInfOp.isPresent()) {
			strategy.getResponseCreator().create(response);
			return false;
		}

		// request-base Info
		AuthRequestMapper.AuthInfo authInfo = authInfOp.get();
		String requestUsername = authInfo.getUsername();

		// developer-defined Info
		AuthFinisher finisher = strategy.getFinisher();
		AuthIdentity authIdentity;
		try {
			authIdentity = strategy.getIdentifier().find(requestUsername);
		} catch (UserIdentityNotFoundException e) {
			finisher.onFail(request, response);
			return false;
		}

		// check match
		boolean isMatch = authIdentity.confirm(authInfo.getPassword());

		if (isMatch) {
			//noinspection unchecked
			sessionManager.set(authIdentity, session);
			finisher.onAuth(requestUsername, session);
		}
		else {
			sessionManager.remove(session);
//			strategy.getResponseCreator().create(response);
			finisher.onFail(request, response);
		}

		return isMatch;
	}
}
