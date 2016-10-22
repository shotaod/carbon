package org.dabuntu.web.server.jetty._escape;

import org.dabuntu.web.context.ApplicationPool;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.HandlerWrapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ubuntu 2016/10/15.
 */
@Deprecated
public class RequestScopeWrapper extends HandlerWrapper {
	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		try {
			ApplicationPool.instance.getRequestPool().setObject(request, HttpServletRequest.class);
			super.handle(target, baseRequest, request, response);
		} finally {
			ApplicationPool.instance.getRequestPool().clear();
		}
	}
}
