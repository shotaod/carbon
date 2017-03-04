package org.carbon.web.handler;

import java.util.List;
import java.util.Optional;
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

    public String getChainResult() {
        return String.format("└─ %s", getChainName()) +
                Optional.ofNullable(handlerChain)
                        .map(HttpHandlerChain::getChainResult)
                        .map(child -> "\n" + child)
                        .orElse("");
    }

    public final HttpHandlerChain setChain(HttpHandlerChain httpHandlerChain) {
        this.handlerChain = httpHandlerChain;
        return httpHandlerChain;
    }

    public final HttpHandlerChain setChains(List<HttpHandlerChain> handlerChains) {
        return handlerChains.stream().reduce(this, HttpHandlerChain::setChain);
    }

    public final void startSync(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("[{}] start sync", getChainName());
        synchronized (lock) {
            chain(request, response);
        }
    }

    public final void startAsync(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("[{}] start async", getChainName());
        chain(request, response);
    }

    protected void chain(HttpServletRequest request, HttpServletResponse response) {
        if (this.handlerChain == null) return;

        logger.debug("[{}] start chain ", this.handlerChain.getChainName());
        this.handlerChain.chain(request, response);
    }
}
