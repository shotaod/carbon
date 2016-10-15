package org.dabuntu.web.core;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.web.container.ActionContainer;
import org.dabuntu.web.container.ControllerAction;
import org.dabuntu.web.container.DefinedAction;
import org.dabuntu.web.container.PathVariableBinding;
import org.dabuntu.web.container.ResolvedArgument;
import org.dabuntu.web.context.MappedActionContainer;
import org.dabuntu.web.def.HttpMethod;
import org.dabuntu.web.exception.ActionNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ubuntu 2016/10/07.
 */
@Component
public class ActionFinder {

	private class UrlMatchResult {
		private boolean match;
		private DefinedAction action;
		private List<ResolvedArgument> rawBindings;

		public UrlMatchResult() {
			rawBindings = new ArrayList<>();
		}

		public void setMatch(boolean match) {
			this.match = match;
		}

		public void setAction(DefinedAction action) {
			this.action = action;
		}

		public void addBind(ResolvedArgument rawBinding) {
			this.rawBindings.add(rawBinding);
		}

		public boolean isMatch() {
			return match;
		}

		public ActionContainer getActionContainer() {
			return new ActionContainer(
				new ControllerAction(action.getControllerClass(), action.getActionMethod()),
				this.rawBindings
			);
		}
	}

	public ActionFinder() {}

	public ActionContainer find(HttpServletRequest request, MappedActionContainer container) {
		// classify by HttpMethod
		List<DefinedAction> bindActions = filterByHttpMethod(request, container);

		// find Action by Url
		ActionContainer bindingResult = findAction(request, bindActions);

		return bindingResult;
	}

	private List<DefinedAction> filterByHttpMethod(HttpServletRequest request, MappedActionContainer container) {
		HttpMethod httpMethod = HttpMethod.codeOf(request.getMethod());
		return container.getContainer().get(httpMethod);
	}
	
	private ActionContainer findAction(HttpServletRequest request, List<DefinedAction> bindActions) {
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

	private UrlMatchResult matchUrl(String requestPath, DefinedAction action) {
		UrlMatchResult result = new UrlMatchResult();
		String[] requestParts = requestPath.split("/");
		String[] computedParts = action.getComputedUriVariableContainer().getComputedUri().split("/");

		if(requestParts.length != computedParts.length) {
			result.setMatch(false);
			return result;
		}

		for (int i = 0; i < requestParts.length; i++) {
			String requestPart = requestParts[i];
			String computedPart = computedParts[i];

			// if PathVariable
			if (computedPart.startsWith("$$")) {
				PathVariableBinding pathVariable = null;
				for (PathVariableBinding defPathVar : action.getComputedUriVariableContainer().getVariables()) {
					if (computedPart.equals(defPathVar.getPathVariableMark())) {
						pathVariable = defPathVar;
						break;
					}
				}
				result.addBind(new ResolvedArgument(
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
