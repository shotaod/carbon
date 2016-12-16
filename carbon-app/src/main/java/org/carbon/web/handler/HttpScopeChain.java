package org.carbon.web.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ubuntu 2016/10/17.
 */
public abstract class HttpScopeChain extends HttpHandlerChain{
	@Override
	protected final void chain(HttpServletRequest request, HttpServletResponse response) {
		try {
			this.in(request, response);
			super.chain(request, response);
		} finally {
			this.out(request, response);
		}
	}

	abstract protected void in(HttpServletRequest request, HttpServletResponse response);
	abstract protected void out(HttpServletRequest request, HttpServletResponse response);
}
