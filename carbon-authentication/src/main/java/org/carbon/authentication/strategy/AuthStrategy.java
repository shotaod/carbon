package org.carbon.authentication.strategy;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.authentication.AuthIdentity;
import org.carbon.authentication.strategy.request.AuthRequest;

/**
 * @author garden 2018/02/12.
 */
public interface AuthStrategy extends AuthEventListener {
    AuthStrategy prototype(HttpServletRequest request, HttpServletResponse response);

    boolean shouldHandle(HttpServletRequest request);

    boolean shouldPermitNoAuth();

    boolean shouldTryAuth();

    boolean shouldExpire();

    boolean existSession();

    Optional<AuthRequest> mapRequest();

    Optional<AuthIdentity> find(AuthRequest authRequest);

    boolean confirm(AuthIdentity authIdentity);
}
