package org.dabuntu.web.auth;

import org.dabuntu.web.context.SessionContainer;
import org.dabuntu.web.def.HttpMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ubuntu 2016/11/03.
 */
public class SecurityConfiguration {
	public class Rule<IDENTITY extends AuthIdentity> {
		private Class<IDENTITY> _identity;
		private SecurityConfiguration baseConfiguration;
		private String _url;
		private String _loginUrl;
		private HttpMethod _method;
		private String _redirect;
		private AuthRequestMapper _requestMapper;
		private AuthResponseCreator _responseCreator;
		private AuthIdentifier<IDENTITY> _identifier;
		private AuthFinisher _finisher;
		private Rule(SecurityConfiguration baseConfiguration, Class<IDENTITY> clazz) {
			this.baseConfiguration = baseConfiguration;
			this._identity = clazz;
		}
		public Rule base(String url) {
			this._url = url;
			return this;
		}
		public Rule endPoint(HttpMethod method, String loginUrl) {
			this._method = method;
			this._loginUrl = loginUrl;
			return this;
		}
		public Rule redirect(String redirectUrl) {
			this._redirect = redirectUrl;
			return this;
		}
		public Rule requestMapper(AuthRequestMapper requestMapper) {
			this._requestMapper = requestMapper;
			return this;
		}
		public Rule responese(AuthResponseCreator responseCreator) {
			this._responseCreator = responseCreator;
			return this;
		}
		public Rule identifier(AuthIdentifier identifier) {
			this._identifier = identifier;
			return this;
		}
		public Rule finisher(AuthFinisher finisher) {
			this._finisher = finisher;
			return this;
		}
		public SecurityConfiguration end() {
			baseConfiguration.rules.add(this);
			return baseConfiguration;
		}

		private AuthStrategy<IDENTITY> convert() {
			AuthStrategy<IDENTITY> strategy = new AuthStrategy<IDENTITY>() {
				@Override
				public boolean shouldTryLogin(HttpMethod method, String requestUrl) {
					return _loginUrl.equals(requestUrl)
						&& _method.equals(method);
				}
			};
			AuthSessionManger<IDENTITY> sessionManger = new AuthSessionManger<IDENTITY>() {
				@Override
				public Optional<IDENTITY> get(SessionContainer session) {
					return session.getObject(_identity);
				}

				@Override
				public void set(IDENTITY identity, SessionContainer session) {
					session.setObject(identity);
				}

				@Override
				public void remove(SessionContainer session) {
					session.removeObject(_identity);
				}
			};
			strategy.setIdentityType(_identity);
			strategy.setBaseUrl(_url);
			strategy.setRedirectUrl(_redirect);
			strategy.setSessionManager(sessionManger);
			strategy.setRequestMapper(_requestMapper);
			strategy.setResponseCreator(_responseCreator);
			strategy.setIdentifier(_identifier);
			strategy.setFinisher(_finisher);
			return strategy;
		}
	}
	private List<Rule> rules;

	public SecurityConfiguration() {
		rules = new ArrayList<>();
	}

	@SuppressWarnings("unchecked")
	public <T extends AuthIdentity> Rule define(Class<T> identityType) {
		return new Rule(this, identityType);
	}

	public List<AuthStrategy> getStrategies() {
		return this.rules.stream()
				.map(rule -> rule.convert())
				.collect(Collectors.toList());
	}
}
