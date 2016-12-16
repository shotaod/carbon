package org.carbon.web.handler;

import org.carbon.web.container.error.ErrorResponseConsumer;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.web.container.error.ErrorHandlingContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ubuntu 2016/10/17.
 */
@Component
public class ErrorWrapperChain extends HttpHandlerChain{

	private Logger logger = LoggerFactory.getLogger(ErrorWrapperChain.class);

	@Inject
	private ErrorHandlingContainer errorHandleRule;

	@Override
	protected void chain(HttpServletRequest request, HttpServletResponse response) {
		try {
			super.chain(request, response);
		} catch (Throwable throwable) {
			if (!this.handleThrowable(response, throwable)) {
				logger.warn("Error that can't be handled is Occurred", throwable);
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				try {
					response.getWriter().println("<h1>Error</h1>");
				} catch (IOException ignored) {}
			} else {
				logger.warn("Handled Error", throwable);
			}
		}
	}

	private boolean handleThrowable(HttpServletResponse response, Throwable throwable) {
		ErrorResponseConsumer errorConsumer = errorHandleRule.getErrorConsumer(throwable);

		if (errorConsumer == null) return false;

		errorConsumer.accept(throwable, response);

		return true;
	}
}
