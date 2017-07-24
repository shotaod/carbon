package org.carbon.authentication;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.carbon.authentication.request.SimpleRequest;
import org.carbon.util.format.StringLineBuilder;
import org.carbon.web.context.session.SessionContext;
import org.carbon.web.def.HttpMethod;
import org.carbon.web.exception.InsufficientSecurityConfigException;

/**
 * @author Shota Oda 2016/11/03.
 */
public class AuthDefinition {
    public class Rule<IDENTITY extends AuthIdentity> {

        private Class<IDENTITY> _identity;
        private AuthDefinition authDefinition;
        private String _url;
        private SimpleRequest _loginRequest;
        private Set<SimpleRequest> _permitRequests;
        private String _logoutUrl;
        private String _redirect;
        private AuthRequestMapper _requestMapper;
        private AuthIdentifier<IDENTITY> _identifier;
        private AuthEventListener _finisher;
        private Rule(AuthDefinition authDefinition) {
            this.authDefinition = authDefinition;
            this._permitRequests = new HashSet<>();
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
            this._loginRequest = new SimpleRequest(method, loginUrl);
            return this;
        }
        public Rule permit(HttpMethod method, String path) {
            _permitRequests.add(new SimpleRequest(method, path));
            return this;
        }
        public Rule permitGetAll(String... path) {
            this._permitRequests.addAll(
                Stream.of(path)
                    .map(url -> new SimpleRequest(HttpMethod.GET, url))
                    .collect(Collectors.toSet())
            );
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
        public AuthDefinition end() {
            StringLineBuilder sb = new StringLineBuilder();
            if (_url == null) sb.appendLine("- url");
            if (_loginRequest == null) sb.appendLine("- endPoint ");
            if (_logoutUrl == null) sb.appendLine("- logoutUrl");
            if (_redirect == null) sb.appendLine("- redirect");
            if (_requestMapper == null) sb.appendLine("- requestMapper");
            if (_identifier == null) sb.appendLine("- identifier");
            if (_finisher == null) sb.appendLine("- finisher");
            if (!sb.toString().isEmpty()) throw new InsufficientSecurityConfigException("below config is not defined\n"+sb.toString());

            authDefinition.rules.add(this);
            return authDefinition;
        }

        private AuthStrategy<IDENTITY> convert() {
            AuthStrategy<IDENTITY> strategy = new AuthStrategy<IDENTITY>() {

                @Override
                public boolean shouldTryLogin(HttpMethod method, String requestUrl) {
                    return _loginRequest.isMatch(method, requestUrl);
                }

                @Override
                public boolean shouldPermit(HttpMethod method, String requestUrl) {
                    return _permitRequests.stream().anyMatch(request -> request.isMatch(method, requestUrl));
                }
            };
            AuthSessionManager<IDENTITY> sessionManger = new AuthSessionManager<IDENTITY>() {
                @Override
                public Optional<IDENTITY> get(SessionContext session) {
                    return session.getByType(_identity);
                }

                @Override
                public void set(AuthIdentity identity, SessionContext session) {
                    session.setObject(identity);
                }

                @Override
                public void remove(SessionContext session) {
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

    public AuthDefinition() {
        rules = new ArrayList<>();
    }

    public Rule define() {
        return new Rule(this);
    }

    public List<AuthStrategy<? extends AuthIdentity>> getStrategies() {
        return this.rules.stream()
                .map(rule -> (AuthStrategy<? extends AuthIdentity>)rule.convert())
                .collect(Collectors.toList());
    }
}
