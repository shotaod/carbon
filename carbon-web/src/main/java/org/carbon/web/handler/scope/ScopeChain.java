package org.carbon.web.handler.scope;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.web.handler.HandlerChain;
import org.slf4j.LoggerFactory;

/**
 * @author Shota Oda 2016/10/17.
 */
public abstract class ScopeChain extends HandlerChain {
    @Override
    protected final void chain(HttpServletRequest request, HttpServletResponse response) {
        try {
            this.in(request, response);
            super.chain(request, response);
        } catch (Throwable t) {
            LoggerFactory.getLogger(ScopeChain.class).error("", t);
        } finally {
            this.out(request, response);
        }
    }

    abstract protected void in(HttpServletRequest request, HttpServletResponse response);

    abstract protected void out(HttpServletRequest request, HttpServletResponse response);
}
