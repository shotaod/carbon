package org.carbon.web.auth;

import org.carbon.web.context.session.SessionContainer;
import org.carbon.web.def.HttpMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Shota Oda 2016/11/03.
 */
public class SecurityConfiguration {
	public class Rule<IDENTITY extends AuthIdentity> {
		private Class<IDENTITY> _identity;
		private SecurityConfiguration baseConfiguration;
		private String _url;
		private String _loginUrl;
		private String _logoutUrl;
		private HttpMethod _method;
		private String _redirect;
		private AuthRequestMapper _requestMapper;
		private AuthIdentifier<IDENTITY> _identifier;
		private AuthEventListener _finisher;
		private Rule(SecurityConfiguration baseConfiguration) {
			this.baseConfiguration = baseConfiguration;
		}
		public Rule base(String url) {
			this._url = url;
			return this;
		}

		/**
		 * set login end point.
		 * @param method
		 * @param loginUrl /xxx/login(single point) or /xxx/** (wildcard)
		 * @return
		 */
		public Rule endPoint(HttpMethod method, String loginUrl) {
			this._method = method;
			this._loginUrl = loginUrl;
			return this;
		}
		public Rule logout(String logoutUrl) {
			this._logoutUrl = logoutUrl;
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
		@SuppressWarnings("unchecked")
		public Rule identifier(AuthIdentifier identifier) {
			this._identity = identifier.getType();
			this._identifier = identifier;
			return this;
		}
		public Rule finisher(AuthEventListener finisher) {
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
					final String wildCard = "/**";
					boolean urlMatch;
					if (_loginUrl.endsWith(wildCard)) {
						urlMatch = requestUrl.startsWith(_loginUrl.replace(wildCard, ""));
					} else {
						urlMatch = _loginUrl.equals(requestUrl);
					}
					return urlMatch && _method.equals(method);
				}
			};
			AuthSessionManager<IDENTITY> sessionManger = new AuthSessionManager<IDENTITY>() {
				@Override
				public Optional<IDENTITY> get(SessionContainer session) {
					return session.getObject(_identity);
				}

				@Override
				public void set(AuthIdentity identity, SessionContainer session) {
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
			strategy.setLogoutUrl(_logoutUrl);
			strategy.setSessionManager(sessionManger);
			strategy.setRequestMapper(_requestMapper);
			strategy.setIdentifier(_identifier);
			strategy.setFinisher(_finisher);
			return strategy;
		}
	}
	private List<Rule> rules;

	public SecurityConfiguration() {
		rules = new ArrayList<>();
	}

	public Rule define() {
		return new Rule(this);
	}

	public List<AuthStrategy<AuthIdentity>> getStrategies() {
		return this.rules.stream()
				.map(rule -> (AuthStrategy<AuthIdentity>)rule.convert())
				.collect(Collectors.toList());
	}
}
