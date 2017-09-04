package org.carbon.authentication;

import java.util.ArrayList;
import java.util.Arrays;
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
        private String[] _baseUrls;
        private SimpleRequest _loginRequest;
        private Set<SimpleRequest> _permitRequests;
        private String _logoutUrl;
        private String _redirect;
        private AuthRequestMapper _requestMapper;
        private AuthIdentifier<IDENTITY> _identifier;
        private AuthEventListener<? super IDENTITY> _eventListener;
        private Rule(AuthDefinition authDefinition) {
            this.authDefinition = authDefinition;
            this._permitRequests = new HashSet<>();
        }
        public Rule<IDENTITY> base(String... baseUrls) {
            this._baseUrls = baseUrls;
            return this;
        }

        /**
         * set login end point.
         * @param method HTTP Method
         * @param loginUrl /xxx/login(single point) or /xxx/** (wildcard)
         */
        public Rule<IDENTITY> endPoint(HttpMethod method, String loginUrl) {
            this._loginRequest = new SimpleRequest(method, loginUrl);
            return this;
        }
        public Rule<IDENTITY> permit(HttpMethod method, String path) {
            _permitRequests.add(new SimpleRequest(method, path));
            return this;
        }
        public Rule<IDENTITY> permitGetAll(String... path) {
            this._permitRequests.addAll(
                Stream.of(path)
                    .map(url -> new SimpleRequest(HttpMethod.GET, url))
                    .collect(Collectors.toSet())
            );
            return this;
        }
        public Rule<IDENTITY> logout(String logoutUrl) {
            this._logoutUrl = logoutUrl;
            return this;
        }
        public Rule<IDENTITY> redirect(String redirectUrl) {
            this._redirect = redirectUrl;
            return this;
        }
        public Rule<IDENTITY> requestMapper(AuthRequestMapper requestMapper) {
            this._requestMapper = requestMapper;
            return this;
        }
        @SuppressWarnings("unchecked")
        public Rule<IDENTITY> identifier(AuthIdentifier identifier) {
            this._identity = identifier.getType();
            this._identifier = identifier;
            return this;
        }
        public Rule<IDENTITY> eventListener(AuthEventListener<? super IDENTITY> eventListener) {
            this._eventListener = eventListener;
            return this;
        }
        @SuppressWarnings("unchecked")
        public AuthDefinition end() {
            StringLineBuilder sb = new StringLineBuilder();
            if (_baseUrls == null) sb.appendLine("- baseUrl");
            if (_loginRequest == null) sb.appendLine("- endPoint ");
            if (_logoutUrl == null) sb.appendLine("- logoutUrl");
            if (_redirect == null) sb.appendLine("- redirect");
            if (_requestMapper == null) sb.appendLine("- requestMapper");
            if (_identifier == null) sb.appendLine("- identifier");
            if (!sb.toString().isEmpty()) throw new InsufficientSecurityConfigException("below config is not defined\n"+sb.toString());

            authDefinition.rules.add((Rule<AuthIdentity>) this);
            return authDefinition;
        }

        private AuthStrategy<IDENTITY> convert() {
            SessionIdentityManager sessionManger = new SessionIdentityManager() {

                @Override
                public void set(AuthIdentity identity, SessionContext session) {
                    session.setObject(identity);
                }

                @Override
                public Optional<? extends AuthIdentity> get(SessionContext session) {
                    return session.getByType(_identity);
                }

                @Override
                public void remove(SessionContext session) {
                    session.removeObject(_identity);
                }
            };
            return new AuthStrategy<IDENTITY>()
                    .setBaseUrls(Arrays.asList(_baseUrls))
                    .setIdentityType(_identity)
                    .setIdentifier(_identifier)
                    .setPermitRequest(_permitRequests)
                    .setLoginRequest(_loginRequest)
                    .setLogoutUrl(_logoutUrl)
                    .setRedirectUrl(_redirect)
                    .setSessionManager(sessionManger)
                    .setRequestMapper(_requestMapper)
                    .setListener(_eventListener);
        }
    }
    private List<Rule<AuthIdentity>> rules;

    public AuthDefinition() {
        rules = new ArrayList<>();
    }

    public <IDENTITY extends AuthIdentity> Rule<IDENTITY> define() {
        return new Rule<>(this);
    }

    public List<AuthStrategy<AuthIdentity>> getStrategies() {
        return this.rules.stream()
                .map(Rule::convert)
                .collect(Collectors.toList());
    }
}
