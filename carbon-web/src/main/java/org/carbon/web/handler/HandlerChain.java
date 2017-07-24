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
public abstract class HandlerChain {
    private Logger logger = LoggerFactory.getLogger(HandlerChain.class);
    private final Object lock = new Object();
    private HandlerChain handlerChain;

    public String getChainName() {
        return this.getClass().getSimpleName();
    }

    public String getChainResult() {
        return String.format("└─ %s", getChainName()) +
                Optional.ofNullable(handlerChain)
                        .map(HandlerChain::getChainResult)
                        .map(child -> "\n" + child)
                        .orElse("");
    }

    public final HandlerChain withChain(HandlerChain handlerChain) {
        this.handlerChain = handlerChain;
        return handlerChain;
    }

    public final HandlerChain setChains(List<HandlerChain> handlerChains) {
        return handlerChains.stream().reduce(this, HandlerChain::withChain);
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
