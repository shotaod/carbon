package org.carbon.authentication;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.authentication.request.SimpleRequest;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.web.context.session.SessionContext;
import org.carbon.web.def.HttpMethod;
import org.carbon.web.exception.UserIdentityNotFoundException;
import org.carbon.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota Oda 2016/10/27.
 */
@Component
public class Authenticator {

    private Logger logger = LoggerFactory.getLogger(Authenticator.class);

    @Inject
    private SessionContext session;
    @Inject
    private AuthStrategyContext authStrategyContext;

    /**
     * @return true if authorized, false if not.
     */
    public boolean authenticate(HttpServletRequest request,
                                HttpServletResponse response) {
        logger.debug("[start  ] -> request: {}", request.getPathInfo());

        // -----------------------------------------------------
        //                                               Check where Security need or not
        //                                               -------
        if (!authStrategyContext.existSecurity()) {
            logger.debug("[end    ] -> No Security in web app");
            return true;
        }

        // -----------------------------------------------------
        //                                               Find Security Strategy
        //                                               -------
        AuthStrategy<AuthIdentity>.StrategyRunner strategyRunner = null;
        for (AuthStrategy<AuthIdentity> strategy : authStrategyContext.getStrategies()) {
            Optional<AuthStrategy<AuthIdentity>.StrategyRunner> candidate = strategy.findStrategy(request, session);
            if (candidate.isPresent()) {
                strategyRunner = candidate.get();
                break;
            }
        }

        if (strategyRunner == null) {
            logger.debug("[end    ] -> No Security Strategy found");
            return true;
        } else {
            logger.debug("[process] -> Found Strategy: {}", strategyRunner);
        }

        // -----------------------------------------------------
        //                                               Check Permit Region or not
        //                                               -------
        boolean shouldPermit = strategyRunner.shouldPermit();
        if (shouldPermit) {
            logger.debug("[end    ] -> Permit caz no need authentication");
            return true;
        }

        // -----------------------------------------------------
        //                                               Logout check
        //                                               -------
        if (strategyRunner.handleLogout()) {
            logger.debug("[end    ] -> Logout Success");
            ResponseUtil.redirect(response, strategyRunner.getRedirectUrl());
            return false;
        }

        // -----------------------------------------------------
        //                                               Check session
        //                                               -------
        if (strategyRunner.checkExistSession()) {
            logger.debug("[end    ] -> Login Success; Found Session Identity");
            return true;
        }

        // -----------------------------------------------------
        //                                               Check should try login or redirect
        //                                               -------
        if (!strategyRunner.shouldTryLogin()) {
            String redirectTo = strategyRunner.getRedirectUrl();
            logger.debug("[end    ] -> Request path is not login url, Redirect to {}", redirectTo);
            ResponseUtil.redirect(response, redirectTo);
            return false;
        }

        // -----------------------------------------------------
        //                                               Try login
        //                                               -------
        logger.debug("[process] -> start try login");
        Optional<AuthRequestMapper.AuthInfo> authInfOp = strategyRunner.mapRequest();
        if (!authInfOp.isPresent()) {
            logger.debug("[end    ] -> Failed to Login, Not Found request info");
            strategyRunner.onFail(request, response);
            return false;
        }

        // request-base Auth info
        AuthRequestMapper.AuthInfo authInfo = authInfOp.get();
        String requestUsername = authInfo.getUsername();

        // defined Auth info
        AuthIdentity authIdentity;
        try {
            authIdentity = strategyRunner.find(requestUsername);
        } catch (UserIdentityNotFoundException e) {
            logger.debug("[end    ] -> Failed to Login, Not Found User named: '{}'", requestUsername);
            strategyRunner.onFail(request, response);
            return false;
        }

        // check match
        boolean isMatch = authIdentity.confirm(authInfo.getPassword());

        if (isMatch) {
            strategyRunner.onAuth(authIdentity);
            logger.debug("[end    ] -> Success Login; User Identity: {}", authIdentity.getClass());
        }
        else {
            strategyRunner.onFail(request, response);
            logger.debug("[end    ] -> Failed to Login, Password does not match user named: '{}'", requestUsername);
        }

        return isMatch;
    }
}
