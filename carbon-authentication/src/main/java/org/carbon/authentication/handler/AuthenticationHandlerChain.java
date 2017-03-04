package org.carbon.authentication.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.authentication.Authenticator;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.web.handler.HttpHandlerChain;

/**
 * @author Shota Oda 2017/03/04.
 */
@Component
public class AuthenticationHandlerChain extends HttpHandlerChain {
    @Inject
    private Authenticator authenticator;

    @Override
    protected void chain(HttpServletRequest request, HttpServletResponse response) {
        boolean isAuthenticated = authenticator.authenticate(request, response);
        if (!isAuthenticated) return;

        super.chain(request, response);
    }
}
