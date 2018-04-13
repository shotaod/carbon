package org.carbon.web.handler;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.util.Describable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota Oda 2016/10/17.
 */
public abstract class HandlerChain implements Describable {
    private Logger logger = LoggerFactory.getLogger(HandlerChain.class);
    private final Object lock = new Object();
    private HandlerChain handlerChain;

    private String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public final String describe() {
        return String.format("└─ %s", getName()) +
                Optional.ofNullable(handlerChain)
                        .map(HandlerChain::describe)
                        .map(child -> "\n" + child)
                        .orElse("");
    }

    public final HandlerChain withChain(HandlerChain handlerChain) {
        this.handlerChain = handlerChain;
        return handlerChain;
    }

    public final HandlerChain withChains(List<HandlerChain> handlerChains) {
        return handlerChains.stream().reduce(this, HandlerChain::withChain);
    }

    public final void startSync(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("[{}] start sync", getName());
        synchronized (lock) {
            try {
                chain(request, response);
            } catch (Throwable ignored) {
                // nothing to do
            }
        }
    }

    public final void startAsync(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("[{}] start async", getName());
        try {
            chain(request, response);
        } catch (Throwable ignored) {
            // nothing to do
        }
    }

    protected void chain(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        if (this.handlerChain == null) return;
        try {
            logger.debug("start chain [{}]", this.handlerChain.getName());
            this.handlerChain.chain(request, response);
        } finally {
            logger.debug("end chain [{}]", this.handlerChain.getName());
        }
    }
}
