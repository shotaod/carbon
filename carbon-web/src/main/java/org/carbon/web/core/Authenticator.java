package org.carbon.web.core;

import org.carbon.component.annotation.Inject;
import org.carbon.web.auth.AuthSessionManager;
import org.carbon.web.auth.AuthStrategy;
import org.carbon.web.context.SecurityContainer;
import org.carbon.web.context.session.SessionContainer;
import org.carbon.web.def.HttpMethod;
import org.carbon.web.util.ResponseUtil;
import org.carbon.component.annotation.Component;
import org.carbon.web.auth.AuthEventListener;
import org.carbon.web.auth.AuthIdentity;
import org.carbon.web.auth.AuthRequestMapper;
import org.carbon.web.exception.UserIdentityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @author Shota Oda 2016/10/27.
 */
@Component
public class Authenticator {

    private Logger logger = LoggerFactory.getLogger(Authenticator.class);

    @Inject
    private SessionContainer session;

    /**
     * @return true if authorized, false if should be authorized,
     */
    public boolean authenticate(SecurityContainer securityContainer,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        logger.debug("[Authenticator] start -> request: {}", request.getPathInfo());

        // -----------------------------------------------------
        //                                               Check where Security need or not
        //                                               -------
        if (!securityContainer.existSecurity()) {
            logger.debug("[Authenticator] end -> No Security in web app");
            return true;
        }

        // -----------------------------------------------------
        //                                               Find Security Strategy
        //                                               -------
        String requestPath = Optional.ofNullable(request.getPathInfo()).orElse("");
        Optional<AuthStrategy<? extends AuthIdentity>> strategyOp = securityContainer.getStrategies().stream()
                .filter(strategy -> requestPath.startsWith(strategy.getBaseUrl()))
                .findFirst();

        if (!strategyOp.isPresent()) {
            logger.debug("[Authenticator] end -> No Security Strategy found");
            return true;
        }

        AuthStrategy<? extends AuthIdentity> strategy = strategyOp.get();
        logger.debug("[Authenticator] process -> Found Strategy: {}", strategy.getClass());


        AuthSessionManager<? extends AuthIdentity> sessionManager = strategy.getSessionManager();
        // -----------------------------------------------------
        //                                               Logout check
        //                                               -------
        if (strategy.getLogoutUrl().equals(requestPath)) {
            sessionManager.remove(session);
            logger.debug("[Authenticator] end -> Logout Success");
            ResponseUtil.redirect(response, strategy.getRedirectUrl());
            return false;
        }

        // -----------------------------------------------------
        //                                               Check session
        //                                               -------
        Optional<? extends AuthIdentity> sessionIdentity = sessionManager.get(session);
        sessionIdentity.map(identity -> identity.getClass().equals(strategy.getIdentityType()));

        if (sessionIdentity.map(identity -> identity.getClass().equals(strategy.getIdentityType())).orElse(false)) {
            logger.debug("[Authenticator] end -> Login Success; Found Session Identity: {}", sessionIdentity.get().getClass());
            return true;
        }

        // -----------------------------------------------------
        //                                               Check should try login or redirect
        //                                               -------
        boolean shouldTry = strategy.shouldTryLogin(HttpMethod.codeOf(request.getMethod()), requestPath);
        if (!shouldTry) {
            String redirectTo = strategy.getRedirectUrl();
            logger.debug("[Authenticator] end -> Request path is not login url, Redirect to {}", redirectTo);
            ResponseUtil.redirect(response, redirectTo);
            return false;
        }

        // -----------------------------------------------------
        //                                               Try login
        //                                               -------
        logger.debug("[Authenticator] process -> start try login");
        Optional<AuthRequestMapper.AuthInfo> authInfOp = strategy.getRequestMapper().map(request);
        if (!authInfOp.isPresent()) {
            logger.debug("[Authenticator] end -> Failed to Login, Not Found request info");
            strategy.getFinisher().onFail(request, response);
            return false;
        }

        // request-base Auth info
        AuthRequestMapper.AuthInfo authInfo = authInfOp.get();
        String requestUsername = authInfo.getUsername();

        // server-side Auth info
        AuthEventListener finisher = strategy.getFinisher();
        AuthIdentity authIdentity;
        try {
            authIdentity = strategy.getIdentifier().find(requestUsername);
        } catch (UserIdentityNotFoundException e) {
            logger.debug("[Authenticator] end -> Failed to Login, Not Found User named: '{}'", requestUsername);
            finisher.onFail(request, response);
            return false;
        }

        // check match
        boolean isMatch = authIdentity.confirm(authInfo.getPassword());

        if (isMatch) {
            sessionManager.set(authIdentity, session);
            finisher.onAuth(requestUsername, session);
            logger.debug("[Authenticator] end -> Success Login; User Identity: {}", authIdentity.getClass());
        }
        else {
            sessionManager.remove(session);
            finisher.onFail(request, response);
            logger.debug("[Authenticator] end -> Failed to Login, Password does not match user named: '{}'", requestUsername);
        }

        return isMatch;
    }
}
