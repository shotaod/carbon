package org.dabuntu.web.core;

import org.dabuntu.web.container.ActionContainer;
import org.dabuntu.web.container.BindPathVariable;
import org.dabuntu.web.container.MappedActionContainer;
import org.dabuntu.web.container.UriBindAction;
import org.dabuntu.web.container.raw.RawUrlVariableBinding;
import org.dabuntu.web.def.HttpMethod;
import org.dabuntu.web.exception.ActionNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ubuntu 2016/10/07.
 */
public class ActionResolver {

	private class UrlMatchResult {
		private boolean match;
		private UriBindAction action;
		private List<RawUrlVariableBinding> rawBindings;

		public UrlMatchResult() {
			rawBindings = new ArrayList<>();
		}

		public void setMatch(boolean match) {
			this.match = match;
		}

		public void setAction(UriBindAction action) {
			this.action = action;
		}

		public void addBind(RawUrlVariableBinding rawBinding) {
			this.rawBindings.add(rawBinding);
		}

		public boolean isMatch() {
			return match;
		}

		public ActionContainer getActionContainer() {
			return new ActionContainer(
				this.action,
				this.rawBindings
			);
		}
	}

	public ActionResolver() {}

	public ActionContainer resolve(HttpServletRequest request, MappedActionContainer container) {
		List<UriBindAction> bindActions = filterByHttpMethod(request, container);

		ActionContainer bindingResult = findAction(request, bindActions);

		return bindingResult;
	}

	private List<UriBindAction> filterByHttpMethod(HttpServletRequest request, MappedActionContainer container) {
		HttpMethod httpMethod = HttpMethod.codeOf(request.getMethod());
		return container.getContainer().get(httpMethod);
	}
	
	private ActionContainer findAction(HttpServletRequest request, List<UriBindAction> bindActions) {
		if (bindActions == null) {
			throw  actionNotFoundException(request);
		}
		return bindActions.stream()
			.map(action -> this.matchUrl(request.getPathInfo(), action))
			.filter(UrlMatchResult::isMatch)
			.findFirst()
			.map(matchResult -> matchResult.getActionContainer())
			.orElseThrow(() -> actionNotFoundException(request));
	}

	private UrlMatchResult matchUrl(String requestPath, UriBindAction action) {
		UrlMatchResult result = new UrlMatchResult();
		String[] requestParts = requestPath.split("/");
		String[] computedParts = action.getUriComputedContainer().getComputedUri().split("/");

		if(requestParts.length != computedParts.length) {
			result.setMatch(false);
			return result;
		}

		for (int i = 0; i < requestParts.length; i++) {
			String requestPart = requestParts[i];
			String computedPart = computedParts[i];

			// if PathVariable
			if (computedPart.startsWith("$$")) {
				BindPathVariable pathVariable = null;
				for (BindPathVariable bindPathVariable : action.getUriComputedContainer().getVariables()) {
					if (computedPart.equals(bindPathVariable.getPathVariableMark())) {
						pathVariable = bindPathVariable;
						break;
					}
				}
				result.addBind(new RawUrlVariableBinding(
					pathVariable,
					requestPart
				));
				continue;
			}

			// if Not Match
			if(!computedPart.equals(requestPart)) {
				result.setMatch(false);
				return result;
			}
		}

		result.setMatch(true);
		result.setAction(action);
		return result;
	}

	private ActionNotFoundException actionNotFoundException(HttpServletRequest request) {
		return new ActionNotFoundException(
				String.format(
						"[request] %s is not found in application action map",
						request.getRequestURI()
				)
		);
	}
}
