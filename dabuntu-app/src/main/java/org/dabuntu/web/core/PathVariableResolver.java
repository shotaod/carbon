package org.dabuntu.web.core;

import org.dabuntu.web.annotation.Action;
import org.dabuntu.web.annotation.PathVariable;
import org.dabuntu.web.container.BindPathVariable;
import org.dabuntu.web.container.ComputedUriContainer;
import org.dabuntu.web.exception.ActionMappingException;

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
		private BindPathVariable pathVariable;

		public Container(String partName) {
			this.partName = partName;
		}

		public Container(String partName, BindPathVariable pathVariable) {
			this.partName = partName;
			this.pathVariable = pathVariable;
		}

		public boolean isVariable() {
			return pathVariable != null;
		}

		public String getPartName() {
			return partName;
		}

		public BindPathVariable getPathVariable() {
			return pathVariable;
		}
	}

	private static final String ComputedVariableMarkPrefix = "$$";
	private static final String PathVariablePrefix = "{";
	private static final String PathVariableSuffix = "}";
	private int variableCounter;
	private List<BindPathVariable> variables;

	public ComputedUriContainer resolve(Method actionMethod) {
		variableCounter = 0;
		variables = new ArrayList<>();

		if (!actionMethod.isAnnotationPresent(Action.class)) {
			throw new IllegalArgumentException();
		}

		// get Annotation @Action
		Action actionAnnotation = actionMethod.getDeclaredAnnotation(Action.class);

		// extract bind target arguments by @PathVariable
		List<Parameter> bindTargets = Arrays.stream(actionMethod.getParameters())
				.filter(param -> param.isAnnotationPresent(PathVariable.class))
				.collect(Collectors.toList());

		// compute url defined by @Action
		String computedUri = Arrays.stream(actionAnnotation.url().split("/"))
				.map(urlPart -> {
					Container container = this.computeUrlPartVariable(urlPart, bindTargets);
					if (container.isVariable()) {
						this.variables.add(container.getPathVariable());
					}
					return container.getPartName();
				})
				.collect(Collectors.joining("/"));

		return new ComputedUriContainer(computedUri, variables);
	}

	private Container computeUrlPartVariable(String urlPart, List<Parameter> bindTargets) {
		if (isNotPathVariablePart(urlPart)) {
			return new Container(urlPart);
		}

		// variable name extracted in @Action(url)
		String name = urlPart.replace(PathVariablePrefix, "").replace(PathVariableSuffix, "");

		for (Parameter arg : bindTargets) {
			String nameDefinedArg = arg.getDeclaredAnnotation(PathVariable.class).value();
			if (name.equals(nameDefinedArg)) {
				String mark = getVariableMark();
				BindPathVariable bindVar = new BindPathVariable(arg.getType(), arg.getName(), mark);
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

