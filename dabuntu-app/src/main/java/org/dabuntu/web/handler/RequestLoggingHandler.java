package org.dabuntu.web.handler;

import org.dabuntu.util.SimpleKeyValue;
import org.dabuntu.util.format.BoxedTitleMessage;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author ubuntu 2016/10/11.
 */
public class RequestLoggingHandler extends HandlerWrapper {

	private static Logger logger = LoggerFactory.getLogger(RequestLoggingHandler.class);

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		Enumeration<String> headerNames = request.getHeaderNames();
		List<SimpleKeyValue> headers = new ArrayList<>();
		while(headerNames.hasMoreElements()) {
			String hName = headerNames.nextElement();
			headers.add(new SimpleKeyValue(hName, request.getHeader(hName)));
		}
		if (headers.size() > 0) {
			headers.add(0, new SimpleKeyValue("Request Headers", "↓↓↓Values Below↓↓↓↓↓↓"));
			headers.add(new SimpleKeyValue("Request Headers", "↑↑↑End Of Values↑↑↑↑↑↑"));
			logger.debug("\n" + BoxedTitleMessage.produceLeft(headers));
		}

		Enumeration<String> parameterNames = request.getParameterNames();
		List<SimpleKeyValue> parameters = new ArrayList<>();
		while (parameterNames.hasMoreElements()) {
			String pName = parameterNames.nextElement();
			parameters.add(new SimpleKeyValue(pName, request.getParameter(pName)));
		}
		if (parameters.size() > 0) {
			parameters.add(0, new SimpleKeyValue("Request Parameters", "↓↓↓Values Below↓↓↓↓↓↓"));
			parameters.add(new SimpleKeyValue("Request Parameters", "↑↑↑End Of Values↑↑↑↑↑↑"));
			logger.debug("\n" + BoxedTitleMessage.produceLeft(parameters));
		}

		super.handle(target, baseRequest, request, response);
	}
}
