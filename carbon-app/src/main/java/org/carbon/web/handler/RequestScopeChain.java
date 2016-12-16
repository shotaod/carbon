package org.carbon.web.handler;

import org.carbon.component.annotation.Component;
import org.carbon.web.context.ApplicationPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ubuntu 2016/10/17.
 */
@Component
public class RequestScopeChain extends HttpScopeChain {
	@Override
	protected void in(HttpServletRequest request, HttpServletResponse response) {
		ApplicationPool.instance.getRequestPool().setObject(request, HttpServletRequest.class);
	}

	@Override
	protected void out(HttpServletRequest request, HttpServletResponse response) {
		ApplicationPool.instance.getRequestPool().clear();
	}
}
