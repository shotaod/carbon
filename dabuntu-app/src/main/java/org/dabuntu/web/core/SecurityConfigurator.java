package org.dabuntu.web.core;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.web.annotation.Action;
import org.dabuntu.web.annotation.Auth;
import org.dabuntu.web.annotation.Controller;
import org.dabuntu.web.auth.AuthStrategy;
import org.dabuntu.web.auth.SecurityConfigAdapter;
import org.dabuntu.web.auth.SecurityConfiguration;
import org.dabuntu.web.context.SecurityContainer;
import org.dabuntu.web.exception.AuthStrategyNotRegisteredException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ubuntu 2016/10/28.
 */
@Component
public class SecurityConfigurator {

	private class AuthAndUrl {
		private AuthStrategy strategy;
		private String url;
		public AuthAndUrl(AuthStrategy strategy, String url) {
			this.strategy = strategy;
			this.url = url;
		}
	}

	public SecurityContainer map(Map<Class, Object> context) {
		return findAdapter(context).map(adapter -> {
			SecurityConfiguration config = new SecurityConfiguration();
			adapter.configure(config);
			List<AuthStrategy> strategies = config.getStrategies();
			return new SecurityContainer(strategies);
		}).orElse(new SecurityContainer());
	}

	private Optional<SecurityConfigAdapter> findAdapter(Map<Class, Object> context) {
		return context.entrySet().stream()
			.filter(entry -> SecurityConfigAdapter.class.isAssignableFrom(entry.getKey()))
			.map(entry -> (SecurityConfigAdapter)entry.getValue())
			.findFirst();
	}

	// TODO @Actionベースのセキュリティ設定もしたい。。。
	private List<AuthAndUrl> findActionBaseAuth(Map<Class, Object> context) {
		return context.entrySet().stream()
			.filter(entry -> entry.getKey().isAnnotationPresent(Controller.class))
			.flatMap(entry -> Arrays.stream(entry.getKey().getDeclaredMethods()))
			.filter(method -> method.isAnnotationPresent(Auth.class))
			.map(method -> {
				Class<? extends AuthStrategy> strategy = method.getAnnotation(Auth.class).strategy();
				AuthStrategy authStrategy = context.entrySet().stream()
						.filter(entry -> strategy.equals(entry.getKey()))
						.map(entry -> (AuthStrategy) entry.getValue())
						.findFirst().orElseThrow(() -> new AuthStrategyNotRegisteredException(strategy.getName() + " is not found in component"));
				Action action = method.getAnnotation(Action.class);
				return new AuthAndUrl(authStrategy, action.url());
			})
			.collect(Collectors.toList());
	}
}
