package org.dabuntu.web.core;

import org.dabuntu.util.format.BoxedTitleMessage;
import org.dabuntu.util.SimpleKeyValue;
import org.dabuntu.util.format.TagAttr;
import org.dabuntu.web.annotation.Action;
import org.dabuntu.web.annotation.Controller;
import org.dabuntu.web.annotation.Default;
import org.dabuntu.web.container.ComputedUriContainer;
import org.dabuntu.web.container.MappedActionContainer;
import org.dabuntu.web.container.UriBindAction;
import org.dabuntu.web.def.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ubuntu 2016/10/05.
 */
@Default
public class ActionMapper {

	private static Logger logger = LoggerFactory.getLogger(ActionMapper.class);

	private class Container {
		ComputedUriContainer computedUriContainer;
		HttpMethod method;
		Class controller;
		Method action;

		public Container(ComputedUriContainer computedUriContainer, HttpMethod method, Class controller, Method action) {
			this.computedUriContainer = computedUriContainer;
			this.method = method;
			this.controller = controller;
			this.action = action;
		}
	}

	private PathVariableResolver pathVariableResolver;

	public ActionMapper() {
		this.pathVariableResolver = new PathVariableResolver();
	}

	public MappedActionContainer map(List<Class> classes) {
		Map<HttpMethod, List<Container>> collect = classes.stream()
				.filter(clazz -> clazz.isAnnotationPresent(Controller.class))
				.flatMap(clazz -> {
					// get actions
					return Arrays.stream(clazz.getMethods())
							// filtering Action
							.filter(method -> method.isAnnotationPresent(Action.class))
							// convert Container
							.map(method -> getContainer(clazz, method));
				})
				.collect(Collectors.groupingBy(
						container -> container.method,
						Collectors.toList()
				));
		Map<HttpMethod, List<UriBindAction>> data = collect.entrySet().stream()
				.map(entry -> {
					List<UriBindAction> bindActions = entry.getValue().stream()
							.map(c -> new UriBindAction(c.computedUriContainer, c.controller, c.action))
							.collect(Collectors.toList());
					return new AbstractMap.SimpleEntry<>(entry.getKey(), bindActions);
				}).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));

		loggingResult(data);

		return new MappedActionContainer(data);
	}

	private Container getContainer(Class controller, Method action) {
		ComputedUriContainer computedUriContainer = pathVariableResolver.resolve(action);

		Action annotation = action.getDeclaredAnnotation(Action.class);
		HttpMethod method = annotation.method();

		return new Container(computedUriContainer, method, controller, action);
	}

	private void loggingResult(Map<HttpMethod, List<UriBindAction>> data) {
		List<SimpleKeyValue> kvs = data.entrySet().stream().flatMap(e -> {
			String hMethod = e.getKey().getCode();
			return e.getValue().stream().map(bindAction -> {
				String computedUri = bindAction.getUriComputedContainer().getComputedUri();
				String actionFQN = bindAction.getClass().getName() + "##" + bindAction.getAction().getName();
				String separator = Stream.generate(() -> " ").limit(5 - hMethod.length()).collect(Collectors.joining("", "", ": "));
				return new SimpleKeyValue(hMethod + separator + computedUri, actionFQN);
			});
		}).collect(Collectors.toList());
		String boxedTitleLines = BoxedTitleMessage.produceLeft(kvs);
		String s = TagAttr.getBuilder("Mapping Result").appendLine(boxedTitleLines).toString();
		logger.debug(s);
	}
}
