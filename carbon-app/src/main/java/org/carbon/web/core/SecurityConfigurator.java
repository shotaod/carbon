package org.carbon.web.core;

import org.carbon.web.annotation.Action;
import org.carbon.web.annotation.Controller;
import org.carbon.web.auth.AuthStrategy;
import org.carbon.web.auth.SecurityConfiguration;
import org.carbon.web.context.SecurityContainer;
import org.carbon.component.annotation.Component;
import org.carbon.web.annotation.Auth;
import org.carbon.web.auth.AuthIdentity;
import org.carbon.web.auth.SecurityConfigAdapter;
import org.carbon.web.exception.AuthStrategyNotRegisteredException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Shota Oda 2016/10/28.
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
			List<AuthStrategy<AuthIdentity>> strategies = config.getStrategies();
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
