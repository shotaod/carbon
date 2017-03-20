package org.carbon.web.core;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.util.mapper.KeyValueMapper;
import org.carbon.web.annotation.PathVariable;
import org.carbon.web.annotation.RequestBody;
import org.carbon.web.annotation.RequestCookie;
import org.carbon.web.annotation.Session;
import org.carbon.web.annotation.Validate;
import org.carbon.web.container.ArgumentMeta;
import org.carbon.web.container.ExecutableAction;
import org.carbon.web.container.PathVariableValues;
import org.carbon.web.context.app.ApplicationContext;
import org.carbon.web.context.request.RequestContext;
import org.carbon.web.context.session.SessionContext;
import org.carbon.web.core.request.RequestMapper;
import org.carbon.web.core.validation.SimpleValidationResult;
import org.carbon.web.core.validation.ValidationResult;

/**
 * @author Shota Oda 2016/10/11.
 */
@Component
public class ActionArgumentAggregator {

    private ApplicationContext applicationContext = ApplicationContext.instance;
    @Inject
    private RequestMapper requestMapper;
    @Inject
    private Validator validator;
    @Inject
    private RequestContext requestContext;
    @Inject
    private SessionContext sessionContext;

    private PathVariableValues pathVariableValues;
    private KeyValueMapper objectMapper = new KeyValueMapper();

    public ActionArgumentAggregator with(PathVariableValues pathVariableValues) {
        this.pathVariableValues = pathVariableValues;
        return this;
    }

    public <T> T find(Class<T> type, InstanceSource source) {
        switch (source) {
            case Request:
                return requestContext.getByType(type);
            case Session:
                return sessionContext.getByType(type).orElse(null);
            case Application:
                return applicationContext.getByType(type);
            default:
                return null;
        }
    }

    public ExecutableAction resolve(Method method, Object instance) {
        HttpServletRequest request = requestContext.getByType(HttpServletRequest.class);

        List<Parameter> parameters = Arrays.asList(method.getParameters());

        // join another resolved to shouldResolves
        Set<ConstraintViolation> constraintViolations = new HashSet<>();
        Map<String, ArgumentMeta> resolvedArguments = new HashMap<>();
        parameters.forEach(parameter -> {
            Class<?> paramType = parameter.getType();
            String paramName = parameter.getName();
            Object resolved;
            // mapping
            if (parameter.isAnnotationPresent(PathVariable.class)) {
                String varName = parameter.getDeclaredAnnotation(PathVariable.class).value();
                resolved = mapPathVariable(pathVariableValues, varName);
            } else if (parameter.isAnnotationPresent(RequestCookie.class)) {
                resolved = mapCookie(request, paramType);
            } else if (parameter.isAnnotationPresent(RequestBody.class)) {
                resolved = mapRequestBody(request, paramType);
            } else if (parameter.isAnnotationPresent(Session.class)) {
                resolved = sessionContext.getByType(paramType).orElse(null);
            } else if (ValidationResult.class.isAssignableFrom(paramType)) {
                ValidationResult vr;
                if (paramType.equals(SimpleValidationResult.class)) {
                    vr = new SimpleValidationResult(constraintViolations);
                } else {
                    vr = new ValidationResult(constraintViolations);
                }
                resolved = vr;
            } else {
                resolved = applicationContext.getByType(paramType);
            }

            resolvedArguments.put(paramName, new ArgumentMeta(parameter, resolved));

            // validation
            if (resolved != null && parameter.isAnnotationPresent(Validate.class)) {
                Set<ConstraintViolation<Object>> validateResult = validator.validate(resolved);
                constraintViolations.addAll(validateResult);
            }
        });


        return new ExecutableAction<>(method.getReturnType(), instance, method, resolvedArguments);
    }

    private String mapPathVariable(PathVariableValues pathVariableValues, String varName) {
        return pathVariableValues.getValue(varName);
    }

    private <T> T mapCookie(HttpServletRequest request, Class<T> mapTo) {
        Map<String, Object> cookies = Arrays.stream(request.getCookies())
                .collect(Collectors.toMap(Cookie::getName, Cookie::getValue));
        return objectMapper.mapAndConstruct(cookies, mapTo);
    }

    private <T> T mapRequestBody(HttpServletRequest request, Class<T> mapTo) {
        return requestMapper.map(request, mapTo);
    }
}
