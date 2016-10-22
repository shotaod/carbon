package org.dabuntu.web.handler;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.util.SimpleKeyValue;
import org.dabuntu.util.format.BoxedTitleMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author ubuntu 2016/10/17.
 */
@Component
public class LoggingChain extends HttpHandlerChain {

	private static Logger logger = LoggerFactory.getLogger(LoggingChain.class);

	@Override
	protected void chain(HttpServletRequest request, HttpServletResponse response) {
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

		super.chain(request, response);
	}
}
