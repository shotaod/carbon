package org.dabuntu.web.core;

import org.dabuntu.web.container.ActionResult;
import org.dabuntu.web.exception.ResponseWriteException;
import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ubuntu 2016/10/05.
 */
public class ActionWriter {
	public void write(Request baseRequest, HttpServletResponse response, ActionResult result) {
		try {
			baseRequest.setHandled(true);

			response.setContentType("text/html;charset=utf-8");
			response.setStatus(HttpServletResponse.SC_OK);


			response.getWriter().println("<h1>Hello Jetty!!</h1>");
			response.getWriter().println(result.getResult());
		} catch (IOException e) {
			throw new ResponseWriteException(e);
		}
	}
}
