package org.dabuntu.web.container.error;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.web.exception.ActionNotFoundException;

import javax.servlet.http.HttpServletResponse;
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
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		});

		rule.put(ActionNotFoundException.class, (e,resp) -> {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
		});

		this._rule = rule;
	}

	public boolean isManaged(Throwable throwable) {
		return this._rule.containsKey(throwable.getClass());
	}

	public ErrorResponseConsumer getErrorConsumer(Throwable throwable) {
		return this._rule.get(throwable.getClass());
	}
}
