package org.carbon.web.core;

import org.carbon.component.annotation.Component;
import org.carbon.util.format.StringLineBuilder;
import org.carbon.web.annotation.PathVariable;
import org.carbon.web.container.ComputedUrl;
import org.carbon.web.exception.ActionMappingException;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Shota Oda 2016/10/08.
 */
@Component
public class PathVariableResolver {

	private class CandidateArgument {
		private Parameter argument;
		private boolean resolved;

		public CandidateArgument(Parameter argument) {
			this.argument = argument;
			this.resolved = false;
		}

		public void setResolved(boolean resolved) {
			this.resolved = resolved;
		}

		public Parameter getArgument() {
			return argument;
		}
		public String getPathVarName() {
            return argument.getDeclaredAnnotation(PathVariable.class).value();
        }
		public boolean isResolved() {
			return resolved;
		}
	}

	private static final String ComputedVariableMarkPrefix = "$$";
	private static final String PathVariablePrefix = "{";
	private static final String PathVariableSuffix = "}";
	private int variableCounter;

    public ComputedUrl resolve(String url, Method method) {

        // extract bind target arguments by @PathVariable
        Stream<Parameter> params = Arrays.stream(method.getParameters());

        return resolve(url, params);
    }

    public ComputedUrl resolve(String url, Stream<Parameter> params) {
        variableCounter = 0;
        List<CandidateArgument> candidates = params
            .filter(param -> param.isAnnotationPresent(PathVariable.class))
            .map(CandidateArgument::new).collect(Collectors.toList());
        if (url.startsWith("/message")) {
            System.out.println("hoge");
        }
        // compute url
		List<ComputedUrl.Path> computedPaths = Arrays.stream(url.split("/"))
				.map(path -> computeUrlPartVariable(path, candidates))
                .collect(Collectors.toList());

		// confirm if no unresolved candidate exist
        StringLineBuilder sb = new StringLineBuilder();
        candidates.forEach(candidate -> {
            if (!candidate.isResolved()) {
                String className = candidate.getArgument().getDeclaringExecutable().getDeclaringClass().getName();
                String methodName = candidate.getArgument().getDeclaringExecutable().getName();
                String pathVarName = candidate.getPathVarName();
                sb.appendLine("At %s#%s", className, methodName);
                sb.appendLine("@PathVariable for name('%s') is not exist in url", pathVarName);
            }
        });
        String error = sb.toString();
        if (!error.isEmpty()) {
            throw new ActionMappingException("Illegal Path Variable Definition is Detected!\n" + error);
        }
        return new ComputedUrl(computedPaths);
    }

	private ComputedUrl.Path computeUrlPartVariable(String path, List<CandidateArgument> candidates) {
		if (isStaticPart(path)) {
			return ComputedUrl.Path.Static(path);
		}

		// variable name extracted in annotation url
		String varName = path.replace(PathVariablePrefix, "").replace(PathVariableSuffix, "");
		String replaceMarker = getVariableMark();

        // check resolved
        candidates.stream()
                .filter(candidate -> !candidate.isResolved())
                .forEach(candidate -> {
                    if (candidate.getPathVarName().equals(varName)) {
                        candidate.setResolved(true);
                    }
                });

		return ComputedUrl.Path.Variable(replaceMarker, varName);
	}

	private boolean isStaticPart(String pathPart) {
		return !(pathPart.startsWith(PathVariablePrefix) && pathPart.endsWith(PathVariableSuffix));
	}

	private String getVariableMark() {
		return ComputedVariableMarkPrefix + this.variableCounter++;
	}
}

