package org.carbon.web.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota Oda 2016/10/17.
 */
public abstract class HttpHandlerChain {
    private Logger logger = LoggerFactory.getLogger(HttpHandlerChain.class);
    private final Object lock = new Object();
    private HttpHandlerChain handlerChain;

    public String getChainName() {
        return this.getClass().getSimpleName();
    }

    public final HttpHandlerChain setChain(HttpHandlerChain httpHandlerChain) {
        this.handlerChain = httpHandlerChain;
        return httpHandlerChain;
    }

    public final void startSync(HttpServletRequest request, HttpServletResponse response) {
        synchronized (lock) {
            this.chain(request, response);
        }
    }

    public final void startAsync(HttpServletRequest request, HttpServletResponse response) {
        this.chain(request, response);
    }

    protected void chain(HttpServletRequest request, HttpServletResponse response) {
        if (this.handlerChain == null) return;

        logger.debug(String.format("[%s] start chain ", getChainName()));
        this.handlerChain.chain(request, response);
    }
}
