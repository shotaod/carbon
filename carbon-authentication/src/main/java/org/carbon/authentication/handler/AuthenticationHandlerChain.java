package org.carbon.authentication.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.authentication.DefaultAuthenticator;
import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.web.handler.HandlerChain;

/**
 * @author Shota Oda 2017/03/04.
 */
@Component
public class AuthenticationHandlerChain extends HandlerChain {
    @Assemble
    private DefaultAuthenticator authenticator;

    @Override
    protected void chain(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        authenticator.authenticate(request, response);
        super.chain(request, response);
    }
}
