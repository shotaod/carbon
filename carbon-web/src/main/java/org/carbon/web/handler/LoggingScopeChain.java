package org.carbon.web.handler;

import org.carbon.component.annotation.Component;
import org.carbon.util.SimpleKeyValue;
import org.carbon.util.format.BoxedTitleMessage;
import org.carbon.util.format.ChapterAttr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Shota Oda 2016/10/17.
 */
@Component
public class LoggingScopeChain extends HttpScopeChain {

	private static Logger logger = LoggerFactory.getLogger(LoggingScopeChain.class);

	@Override
	protected void in(HttpServletRequest request, HttpServletResponse response) {
		Enumeration<String> headerNames = request.getHeaderNames();
		List<SimpleKeyValue> headers = new ArrayList<>();
		while(headerNames.hasMoreElements()) {
			String hName = headerNames.nextElement();
			headers.add(new SimpleKeyValue(hName, request.getHeader(hName)));
		}
		if (headers.size() > 0) {
			headers.add(0, new SimpleKeyValue("Request Headers", "↓↓↓Values Below↓↓↓↓↓↓"));
			headers.add(new SimpleKeyValue("Request Headers", "↑↑↑End Of Values↑↑↑↑↑↑"));
			logger.debug(ChapterAttr.getBuilder("Request").appendLine(BoxedTitleMessage.produceLeft(headers)).toString());
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
			logger.debug(ChapterAttr.getBuilder("Request Params").appendLine(BoxedTitleMessage.produceLeft(parameters)).toString());
		}
	}

	@Override
	protected void out(HttpServletRequest request, HttpServletResponse response) {
        List<SimpleKeyValue> headers = new ArrayList<>();
        List<SimpleKeyValue> values = response.getHeaderNames().stream()
                .map(name -> new SimpleKeyValue(name, response.getHeader(name)))
                .collect(Collectors.toList());
        headers.addAll(values);
        headers.add(0, new SimpleKeyValue("Response Headers", "↓↓↓Values Below↓↓↓↓↓↓"));
        headers.add(new SimpleKeyValue("Response Headers", "↑↑↑End Of Values↑↑↑↑↑↑"));
        logger.debug(ChapterAttr.getBuilder("Response").appendLine(BoxedTitleMessage.produceLeft(headers)).toString());
    }
}
