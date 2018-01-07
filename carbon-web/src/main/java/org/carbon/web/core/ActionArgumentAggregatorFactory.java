package org.carbon.web.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Enumeration;
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
import org.carbon.web.annotation.RequestHeader;
import org.carbon.web.annotation.RequestParam;
import org.carbon.web.annotation.Session;
import org.carbon.web.annotation.Validate;
import org.carbon.web.container.ArgumentMeta;
import org.carbon.web.container.ExecutableAction;
import org.carbon.web.container.PathVariableValues;
import org.carbon.web.context.app.ApplicationContext;
import org.carbon.web.context.request.RequestContext;
import org.carbon.web.context.session.SessionContext;
import org.carbon.web.core.request.RequestBodyMapper;
import org.carbon.web.core.validation.ValidationResult;

/**
 * @author Shota Oda 2016/10/11.
 */
@Component
public class ActionArgumentAggregatorFactory {
    private ApplicationContext applicationContext = ApplicationContext.instance;
    @Inject
    private RequestContext requestContext;
    @Inject
    private SessionContext sessionContext;

    @Inject
    private RequestBodyMapper requestBodyMapper;
    @Inject
    private Validator validator;
    @Inject
    private KeyValueMapper keyValueMapper;

    public ActionArgumentAggregator newAggregator(PathVariableValues pathVariableValues) {
        return new ActionArgumentAggregator(pathVariableValues);
    }

    public class ActionArgumentAggregator {
        private PathVariableValues pathVariableValues;

        public ActionArgumentAggregator(PathVariableValues pathVariableValues) {
            this.pathVariableValues = pathVariableValues;
        }

        public <T> T find(Class<T> type, InstanceSource source) {
            return ActionArgumentAggregatorFactory.this.find(type, source);
        }

        public ExecutableAction resolve(Method method, Object instance) {
            return ActionArgumentAggregatorFactory.this.resolve(method, instance, pathVariableValues);
        }
    }

    private <T> T find(Class<T> type, InstanceSource source) {
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

    private ExecutableAction resolve(Method method, Object instance, PathVariableValues pvVals) {
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
                resolved = mapPathVariable(pvVals, varName, paramType);
            } else if (parameter.isAnnotationPresent(RequestCookie.class)) {
                resolved = mapCookie(request, paramType);
            } else if (parameter.isAnnotationPresent(RequestParam.class)) {
                String paramKey = parameter.getDeclaredAnnotation(RequestParam.class).value();
                resolved = mapParam(request, paramKey, paramType);
            } else if (parameter.isAnnotationPresent(RequestHeader.class)) {
                String headerKey = parameter.getDeclaredAnnotation(RequestHeader.class).value();
                resolved = mapHeader(request, headerKey, paramType);
            } else if (parameter.isAnnotationPresent(RequestBody.class)) {
                resolved = mapRequestBody(request, paramType);
            } else if (parameter.isAnnotationPresent(Session.class)) {
                resolved = sessionContext.getByType(paramType).orElse(null);
            } else if (ValidationResult.class.isAssignableFrom(paramType)) {
                try {
                    @SuppressWarnings("unchecked")
                    Constructor<ValidationResult> constructor = (Constructor<ValidationResult>) paramType.getConstructor(Set.class);
                    HashSet<Object> arg = new HashSet<>(constraintViolations);
                    resolved = constructor.newInstance(arg);
                } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ignore) {
                    resolved = null;
                }
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

    private Object mapPathVariable(PathVariableValues pathVariableValues, String varName, Class<?> paramType) {
        String value = pathVariableValues.getValue(varName);
        return keyValueMapper.mapPrimitive(value, paramType);
    }

    private <T> T mapCookie(HttpServletRequest request, Class<T> mapTo) {
        Map<String, Object> cookies = Arrays.stream(request.getCookies())
                .collect(Collectors.toMap(Cookie::getName, Cookie::getValue));
        return keyValueMapper.mapAndConstruct(cookies, mapTo);
    }

    private <T> T mapParam(HttpServletRequest request, String paramKey, Class<T> mapTo) {
        if (paramKey.isEmpty()) {
            Map<String, Object> map = request.getParameterMap().entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> mapParamValue(e.getValue())
                    ));
            return keyValueMapper.mapAndConstruct(map, mapTo);
        }
        if (List.class.isAssignableFrom(mapTo)) {
            String[] vals = request.getParameterValues(paramKey);
            List<String> strs = Arrays.asList(vals);
            // todo: resolve generics (ex. List<T>)
            return (T)strs;
        }

        String param = request.getParameter(paramKey);
        return keyValueMapper.mapPrimitive(param, mapTo);
    }

    private Object mapParamValue(String... params) {
        if (params.length == 0) {
            return null;
        }
        if (params.length == 1) {
            return params[0];
        }
        return params;
    }

    private <T> T mapHeader(HttpServletRequest request, String headerKey, Class<T> mapTo) {
        if (headerKey.isEmpty()) {
            Enumeration<String> headers = request.getHeaderNames();
            Map<String, Object> headerMap = new HashMap<>();
            while (headers.hasMoreElements()) {
                String headerName = headers.nextElement();
                headerMap.put(headerName, request.getHeader(headerName));
            }
            return keyValueMapper.mapAndConstruct(headerMap, mapTo);
        } else {
            String value = request.getHeader(headerKey);
            return keyValueMapper.mapPrimitive(value, mapTo);
        }
    }

    private <T> T mapRequestBody(HttpServletRequest request, Class<T> mapTo) {
        return requestBodyMapper.map(request, mapTo);
    }
}
