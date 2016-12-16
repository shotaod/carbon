package org.carbon.web.core;

import org.carbon.web.annotation.PathVariable;
import org.carbon.web.annotation.Action;
import org.carbon.web.container.ComputedUriVariableContainer;
import org.carbon.web.container.PathVariableBinding;
import org.carbon.web.exception.ActionMappingException;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ubuntu 2016/10/08.
 */
public class PathVariableResolver {

	private class Container {
		private String partName;
		private PathVariableBinding pathVariable;

		public Container(String partName) {
			this.partName = partName;
		}

		public Container(String partName, PathVariableBinding pathVariable) {
			this.partName = partName;
			this.pathVariable = pathVariable;
		}

		public boolean isVariable() {
			return pathVariable != null;
		}

		public String getPartName() {
			return partName;
		}

		public PathVariableBinding getPathVariable() {
			return pathVariable;
		}
	}

	private class CandidateArgument {
		private Parameter argument;
		private boolean resolved;

		public CandidateArgument(Parameter argument) {
			this.argument = argument;
			this.resolved = false;
		}

		public CandidateArgument(Parameter argument, boolean resolved) {
			this.argument = argument;
			this.resolved = resolved;
		}

		public void setResolved(boolean resolved) {
			this.resolved = resolved;
		}

		public Parameter getArgument() {
			return argument;
		}
		public boolean isResolved() {
			return resolved;
		}
	}


	private static final String ComputedVariableMarkPrefix = "$$";
	private static final String PathVariablePrefix = "{";
	private static final String PathVariableSuffix = "}";
	private int variableCounter;
	private List<PathVariableBinding> variables;

	public ComputedUriVariableContainer resolve(Method actionMethod) {
		variableCounter = 0;
		variables = new ArrayList<>();

		if (!actionMethod.isAnnotationPresent(Action.class)) {
			throw new IllegalArgumentException();
		}

		// forClient Annotation @Action
		Action actionAnnotation = actionMethod.getDeclaredAnnotation(Action.class);

		// extract bind target arguments by @PathVariable
		List<CandidateArgument> candidates = Arrays.stream(actionMethod.getParameters())
				.filter(param -> param.isAnnotationPresent(PathVariable.class))
				.map(CandidateArgument::new)
				.collect(Collectors.toList());

		// compute url defined by @Action
		String computedUri = Arrays.stream(actionAnnotation.url().split("/"))
				.map(urlPart -> {
					Container container = this.computeUrlPartVariable(urlPart, candidates);
					if (container.isVariable()) {
						this.variables.add(container.getPathVariable());
					}
					return container.getPartName();
				})
				.collect(Collectors.joining("/"));

		// confirm if no unresolved candidate exist
		candidates.forEach(candidate -> {
			if (!candidate.isResolved()) {
				String controllerClassName = actionMethod.getDeclaringClass().getName();
				String  pathVarName = candidate.getArgument().getAnnotation(PathVariable.class).value();
				throw new ActionMappingException(String.format("Failed to map action. \n At [controller] %s\n    [    action] %s\n PathVariable which named '%s' is not exist in @Action.url", controllerClassName, actionMethod.getName(), pathVarName));
			}
		});

		return new ComputedUriVariableContainer(computedUri, variables);
	}

	private Container computeUrlPartVariable(String urlPart, List<CandidateArgument> candidates) {
		if (isNotPathVariablePart(urlPart)) {
			return new Container(urlPart);
		}

		// variable name extracted in @Action(url)
		String name = urlPart.replace(PathVariablePrefix, "").replace(PathVariableSuffix, "");

		for (CandidateArgument candidate : candidates) {
			Parameter arg = candidate.getArgument();
			String nameDefinedArg = arg.getDeclaredAnnotation(PathVariable.class).value();
			if (name.equals(nameDefinedArg)) {
				String mark = getVariableMark();
				PathVariableBinding bindVar = new PathVariableBinding(arg.getType(), arg.getName(), mark);

				candidate.setResolved(true);

				return new Container(mark, bindVar);
			}
		}

		throw new ActionMappingException(String.format("url binding exception.\nPathVariable (name:'%s') is not found", urlPart));
	}

	private boolean isNotPathVariablePart(String pathPart) {
		return !(pathPart.startsWith(PathVariablePrefix) && pathPart.endsWith(PathVariableSuffix));
	}

	private String getVariableMark() {
		return ComputedVariableMarkPrefix + this.variableCounter++;
	}
}

