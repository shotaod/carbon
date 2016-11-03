package org.dabuntu.web.container.error;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.web.exception.ActionNotFoundException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ubuntu 2016/10/18.
 */
@Component
public class ErrorHandlingContainer {
	private Map<Class<? extends Throwable>, ErrorResponseConsumer> _rule;

	public ErrorHandlingContainer() {
		Map<Class<? extends Throwable>, ErrorResponseConsumer> rule = new HashMap<>();

		rule.put(Exception.class, (e, resp) -> {
			int status = HttpServletResponse.SC_BAD_REQUEST;
			resp.setStatus(status);
			errorPage(status, "Bad Request", resp);
		});

		rule.put(ActionNotFoundException.class, (e,resp) -> {
			int status = HttpServletResponse.SC_NOT_FOUND;
			resp.setStatus(status);
			errorPage(status, "Not Found", resp);
		});

		this._rule = rule;
	}

	public ErrorResponseConsumer getErrorConsumer(Throwable throwable) {
		return this._rule.get(throwable.getClass());
	}

	private void errorPage(int status, String message, HttpServletResponse response) {

		response.setContentType("text/html;charset=UTF-8");

		PrintWriter writer = null;
		try {
			writer = response.getWriter();
		} catch (IOException ignored) {}

		writer.println("<h1>" + status + "</h1>");
		writer.println("<h3>" + message + "</h3>");
	}
}
