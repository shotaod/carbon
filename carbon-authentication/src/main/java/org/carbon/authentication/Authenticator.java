package org.carbon.authentication;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.authentication.strategy.AuthStrategy;
import org.carbon.authentication.strategy.AuthStrategyContext;
import org.carbon.authentication.strategy.request.AuthRequest;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota Oda 2016/10/27.
 */
@Component
public class Authenticator {

    private Logger logger = LoggerFactory.getLogger(Authenticator.class);

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
        Optional<AuthStrategy> strategyOp = authStrategyContext.findStrategy(request, response);

        if (!strategyOp.isPresent()) {
            logger.debug("[end    ] -> No Security Strategy found");
            return true;
        }
        AuthStrategy strategy = strategyOp.get();
        logger.debug("[process] -> Found Strategy: {}", strategy);

        // -----------------------------------------------------
        //                                               Check Permit Region or not
        //                                               -------
        if (strategy.shouldPermitNoAuth()) {
            logger.debug("[end    ] -> Permit caz no need authentication");
            strategy.onPermitNoAuth();
            return true;
        }

        // -----------------------------------------------------
        //                                               Expire check
        //                                               -------
        if (strategy.shouldExpire()) {
            logger.debug("[end    ] -> Expire Success");
            strategy.onExpire();
            return false;
        }

        // -----------------------------------------------------
        //                                               Check session
        //                                               -------
        if (strategy.existSession()) {
            logger.debug("[end    ] -> Auth Success; Found Session Identity");
            strategy.onExistSession();
            return true;
        }

        // -----------------------------------------------------
        //                                               Check should try auth or not
        //                                               -------
        if (!strategy.shouldTryAuth()) {
            logger.debug("[end    ] -> Request path is not auth url, Redirect to {}");
            strategy.onProhibitNoAuth();
            return false;
        }

        // -----------------------------------------------------
        //                                               Try Auth
        //                                               -------
        logger.debug("[process] -> start try auth");
        Optional<AuthRequest> authInfOp = strategy.mapRequest();
        if (!authInfOp.isPresent()) {
            logger.debug("[end    ] -> Failed to auth, Not Found request info");
            strategy.onIllegalAuthRequest();
            return false;
        }

        // request-base Auth info
        AuthRequest authRequest = authInfOp.get();

        // defined Auth info
        Optional<? extends AuthIdentity> authIdentityOp = strategy.find(authRequest);
        if (!authIdentityOp.isPresent()) {
            logger.debug("[end    ] -> Failed to auth, Not found identityClass for request: '{}'", authRequest);
            strategy.onNoFoundIdentity();
            return false;
        }

        AuthIdentity authIdentity = authIdentityOp.get();
        boolean authSuccess = strategy.confirm(authIdentity);
        if (authSuccess) {
            logger.debug("[end    ] -> Success Login; Auth Identity: {}", authIdentity.getClass());
            strategy.onAuth(authIdentity);
        } else {
            logger.debug("[end    ] -> Failed to auth: for identityClass'{}'", authIdentity);
            strategy.onNoMatchSecret();
        }

        return authSuccess;
    }
}
