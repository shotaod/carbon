package org.carbon.web.handler.scope;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.web.context.session.SessionManager;

/**
 * @author Shota Oda 2016/10/17.
 */
@Component
public class SessionScopeChain extends ScopeChain {

    @Assemble
    private SessionManager sessionManager;

    @Override
    protected void in(HttpServletRequest request, HttpServletResponse response) {
        sessionManager.start(request, response);
    }

    @Override
    protected void out(HttpServletRequest request, HttpServletResponse response) {
        sessionManager.end();
    }
}
