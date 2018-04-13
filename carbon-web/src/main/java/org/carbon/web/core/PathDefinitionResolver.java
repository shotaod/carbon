package org.carbon.web.core;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.carbon.component.annotation.Component;
import org.carbon.util.format.StringLineBuilder;
import org.carbon.web.annotation.PathVariable;
import org.carbon.web.container.ComputedPath;
import org.carbon.web.exception.ActionMappingException;

/**
 * @author Shota Oda 2016/10/08.
 */
@Component
public class PathDefinitionResolver {

    private class Counter {
        private int _n = 0;

        public Counter() {
        }

        public int incrementAndGet() {
            return _n++;
        }
    }

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

    public ComputedPath resolve(Path path, Method method) {
        Stream<Parameter> params = Arrays.stream(method.getParameters());
        return resolve(path.toString(), params);
    }

    public ComputedPath resolve(String path, Stream<Parameter> params) {
        List<CandidateArgument> candidates = params
                .filter(param -> param.isAnnotationPresent(PathVariable.class))
                .map(CandidateArgument::new).collect(Collectors.toList());
        // compute path
        List<ComputedPath.Node> computedPaths = Arrays.stream(path.split("/"))
                .map(p -> convertPathPart(p, candidates, new Counter()))
                .collect(Collectors.toList());

        // confirm if no unresolved candidate exist
        StringLineBuilder sb = new StringLineBuilder();
        candidates.forEach(candidate -> {
            if (!candidate.isResolved()) {
                String className = candidate.getArgument().getDeclaringExecutable().getDeclaringClass().getName();
                String methodName = candidate.getArgument().getDeclaringExecutable().getName();
                String pathVarName = candidate.getPathVarName();
                sb.appendLine("At %s#%s", className, methodName);
                sb.appendLine("@PathVariable for name('%s') is not exist in path definition", pathVarName);
            }
        });
        String error = sb.toString();
        if (!error.isEmpty()) {
            throw new ActionMappingException("Illegal Node Variable Definition is Detected!\n" + error);
        }
        return new ComputedPath(computedPaths);
    }

    private ComputedPath.Node convertPathPart(String path, List<CandidateArgument> candidates, Counter variableCounter) {
        if (isStaticPart(path)) {
            return ComputedPath.Node.Static(path);
        }

        // variable name extracted from annotate path
        String varName = path.replace(PathVariablePrefix, "").replace(PathVariableSuffix, "");
        String replaceMarker = getVariableMark(variableCounter);

        // check resolved
        candidates.stream()
                .filter(candidate -> !candidate.isResolved())
                .forEach(candidate -> {
                    if (candidate.getPathVarName().equals(varName)) {
                        candidate.setResolved(true);
                    }
                });

        return ComputedPath.Node.Variable(replaceMarker, varName);
    }

    private boolean isStaticPart(String pathPart) {
        return !(pathPart.startsWith(PathVariablePrefix) && pathPart.endsWith(PathVariableSuffix));
    }

    private String getVariableMark(Counter variableCounter) {
        return ComputedVariableMarkPrefix + variableCounter.incrementAndGet();
    }
}

