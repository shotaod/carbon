package org.carbon.web.handler;

import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ubuntu 2016/10/17.
 */
public abstract class HttpHandlerChain {
	private final Object lock = new Object();
	private HttpHandlerChain handlerChain;

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
		LoggerFactory.getLogger(this.getClass()).debug("start chain");
		if (this.handlerChain == null) return;

		this.handlerChain.chain(request, response);
	}
}
