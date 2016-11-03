package org.dabuntu.web.core;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.util.mapper.NameBasedObjectMapper;
import org.dabuntu.web.annotation.RequestBody;
import org.dabuntu.web.annotation.RequestCookie;
import org.dabuntu.web.annotation.Session;
import org.dabuntu.web.container.ActionContainer;
import org.dabuntu.web.container.ResolvedArgument;
import org.dabuntu.web.container.request.MappedCookie;
import org.dabuntu.web.container.request.MappedRequestBody;
import org.dabuntu.web.container.request.ParsedRequest;
import org.dabuntu.web.context.SessionContainer;
import org.dabuntu.web.def.HttpMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ubuntu 2016/10/11.
 */
@Component
public class ActionArgumentResolver {

	private NameBasedObjectMapper objectMapper = new NameBasedObjectMapper();

	public ActionContainer resolve(ParsedRequest parsedRequest, SessionContainer sessionContainer, ActionContainer actionContainer) {

		Method action = actionContainer.getControllerAction().getAction();
		Parameter[] parameters = action.getParameters();
		// params defined in Method
		List<Parameter> methodParameters = Arrays.asList(parameters);

		// params that Resolved
		List<ResolvedArgument> resolvedArguments = actionContainer.getResolvedArguments();

		// params that should be resolved
		List<Parameter> shouldResolves = extractShouldResolveArguments(methodParameters, resolvedArguments);

		// add another resolved to shouldResolves
		shouldResolves.forEach(parameter -> {
			if (parameter.isAnnotationPresent(RequestCookie.class)) {
				Object resolveCookie = this.mappingCookie(parsedRequest.getMappedCookie(), parameter.getType());
				resolvedArguments.add(new ResolvedArgument(parameter.getName(), resolveCookie));
			} else if (parameter.isAnnotationPresent(RequestBody.class) && !parsedRequest.getMethod().equals(HttpMethod.GET)) {
				Object resolvedReqBody = this.mappingRequest(parsedRequest.getMappedRequestBody(), parameter.getType());
				resolvedArguments.add(new ResolvedArgument(parameter.getName(), resolvedReqBody));
			}
			else if (parameter.isAnnotationPresent(Session.class)) {
				Object sessionObj = sessionContainer.getObject(parameter.getType()).orElse(null);
				resolvedArguments.add(new ResolvedArgument(parameter.getName(), sessionObj));
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

	private <T> T mappingCookie(MappedCookie cookie, Class<T> mapTo) {
		Map<String, Object> collect = cookie.getCookies().entrySet().stream()
				.collect(Collectors.toMap(
						entry -> entry.getKey(),
						entry -> (Object) entry.getValue()
				));
		return objectMapper.map(collect, mapTo);
	}

	private <T> T mappingRequest(MappedRequestBody request, Class<T> castTo) {
		return objectMapper.map(request.getRequests(), castTo);
	}
}
