package org.carbon.web.core;

import org.carbon.web.annotation.RequestBody;
import org.carbon.web.annotation.RequestCookie;
import org.carbon.web.annotation.Session;
import org.carbon.web.annotation.Validate;
import org.carbon.web.container.ResolvedArgument;
import org.carbon.web.context.SessionContainer;
import org.carbon.web.core.request.RequestMapper;
import org.carbon.web.core.validation.SimpleValidationResult;
import org.carbon.web.core.validation.ValidationResult;
import org.carbon.web.def.HttpMethod;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.util.mapper.NameBasedObjectMapper;
import org.carbon.web.container.ActionContainer;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Shota Oda 2016/10/11.
 */
@Component
public class ActionArgumentResolver {

	@Inject
	private RequestMapper requestMapper;
	@Inject
	private NameBasedObjectMapper objectMapper;
    @Inject
    private Validator validator;

	public ActionContainer resolve(HttpServletRequest request, SessionContainer sessionContainer, ActionContainer actionContainer) {

		HttpMethod method = HttpMethod.codeOf(request.getMethod());

		Method action = actionContainer.getControllerAction().getAction();
		Parameter[] parameters = action.getParameters();
		// params defined in Method
		List<Parameter> methodParameters = Arrays.asList(parameters);

		// params that is resolved
		List<ResolvedArgument> resolvedArguments = actionContainer.getResolvedArguments();

		// params that should be resolved
		List<Parameter> shouldResolves = extractShouldResolveArguments(methodParameters, resolvedArguments);

		// add another resolved to shouldResolves
        Set<ConstraintViolation> constraintViolations = new HashSet<>();
        shouldResolves.forEach(parameter -> {
            Class<?> paramType = parameter.getType();
            if (parameter.isAnnotationPresent(RequestCookie.class)) {
				Object resolveCookie = mapCookie(request, paramType);
				resolvedArguments.add(new ResolvedArgument(parameter.getName(), resolveCookie));
			} else if (parameter.isAnnotationPresent(RequestBody.class) && !method.equals(HttpMethod.GET)) {
				Object resolvedReqBody = mapRequestBody(request, paramType);
                if (parameter.isAnnotationPresent(Validate.class)) {
                    constraintViolations.addAll(validator.validate(resolvedReqBody));
                }
                resolvedArguments.add(new ResolvedArgument(parameter.getName(), resolvedReqBody));
			} else if (parameter.isAnnotationPresent(Session.class)) {
				Object sessionObj = sessionContainer.getObject(paramType).orElse(null);
				resolvedArguments.add(new ResolvedArgument(parameter.getName(), sessionObj));
			} else if (ValidationResult.class.isAssignableFrom(paramType)) {
                ValidationResult vr;
				if (paramType.equals(SimpleValidationResult.class)) {
                    vr = new SimpleValidationResult(constraintViolations);
                } else {
                    vr = new ValidationResult(constraintViolations);
                }
                resolvedArguments.add(new ResolvedArgument(parameter.getName(), vr));
            }
		});

		return new ActionContainer(actionContainer.getAuth(), actionContainer.getControllerAction(), resolvedArguments);
	}

	/**
	 *  @param margs are All Method Args
	 * @param rargs are Resolved Args
	 */
	private List<Parameter> extractShouldResolveArguments(List<Parameter> margs, List<ResolvedArgument> rargs) {
		return margs.stream().filter(marg -> {
			// filtering not resolve
			return rargs.stream().noneMatch(rarg -> rarg.equals(marg));
		}).collect(Collectors.toList());
	}

	private <T> T mapCookie(HttpServletRequest request, Class<T> mapTo) {
		Map<String, Object> cookies = Arrays.stream(request.getCookies()).collect(Collectors.toMap(
			cookie -> cookie.getName(),
			cookie -> cookie.getValue()
		));
		return objectMapper.map(cookies, mapTo);
	}

	private <T> T mapRequestBody(HttpServletRequest request, Class<T> mapTo) {
		return requestMapper.map(request, mapTo);
	}
}
