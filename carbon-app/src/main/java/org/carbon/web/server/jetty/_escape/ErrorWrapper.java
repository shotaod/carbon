package org.carbon.web.server.jetty._escape;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ubuntu 2016/10/11.
 */
@Deprecated
public class ErrorWrapper extends HandlerWrapper {
	private static Logger logger = LoggerFactory.getLogger(ErrorWrapper.class);

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		try {
			super.handle(target, baseRequest, request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
