package org.carbon.authentication;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.authentication.request.SimpleRequest;
import org.carbon.util.SimpleKeyValue;
import org.carbon.util.format.BoxedTitleMessage;
import org.carbon.web.context.session.SessionContext;
import org.carbon.web.exception.UserIdentityNotFoundException;

/**
 * @author Shota Oda 2016/10/27.
 */
public class AuthStrategy<IDENTITY extends AuthIdentity> {
    private List<String> baseUrls;
    private Class<IDENTITY> identityType;
    private AuthIdentifier<IDENTITY> identifier;
    private SimpleRequest loginRequest;
    private String logoutUrl;
    private String redirectUrl;
    private Set<SimpleRequest> permitRequest;

    private SessionIdentityManager sessionManager;
    private AuthRequestMapper requestMapper;
    private AuthEventListener<? super IDENTITY> listener;

    public class StrategyRunner implements
            AuthIdentifier<IDENTITY>
    {
        private SimpleRequest request;
        private SessionContext sessionContext;

        public StrategyRunner(SimpleRequest request, SessionContext sessionContext) {
            this.request = request;
            this.sessionContext = sessionContext;
        }

        public boolean shouldPermit() {
            return permitRequest.stream().anyMatch(permit -> permit.isMatch(request));
        }
        public boolean shouldTryLogin() {
            return loginRequest.isMatch(request);
        }
        public boolean handleLogout() {
            return logoutUrl.equals(request.getPath());
        }
        public boolean checkExistSession() {
            return sessionManager.get(sessionContext).isPresent();
        }
        public String getRedirectUrl() {
            return redirectUrl;
        }
        public Optional<AuthRequestMapper.AuthInfo> mapRequest() {
            return requestMapper.map(request.getBaseRequest());
        }

        // -----------------------------------------------------
        //                                               AuthEventListener
        //                                               -------
        public void onAuth(IDENTITY identity) {
            sessionContext.setObject(identity);
            listener.onAuth(identity, sessionContext);
        }
        public void onFail(HttpServletRequest request, HttpServletResponse response) {
            sessionContext.removeObject(identityType);
            listener.onFail(request, response);
        }
        // -----------------------------------------------------
        //                                               AuthIdentifier
        //                                               -------
        public Class<IDENTITY> getType() {
            return identifier.getType();
        }
        public IDENTITY find(String username) throws UserIdentityNotFoundException {
            return identifier.find(username);
        }
    }

    public Optional<StrategyRunner> findStrategy(HttpServletRequest request, SessionContext context) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            pathInfo = "";
        }
        if (baseUrls.stream().anyMatch(pathInfo::startsWith)) {
            SimpleRequest simpleRequest = new SimpleRequest(request);
            return Optional.of(new StrategyRunner(simpleRequest, context));
        }

        return Optional.empty();
    }

    // -----------------------------------------------------
    //                                               Setter
    //                                               -------
    public AuthStrategy<IDENTITY> setBaseUrls(List<String> baseUrls) {
        this.baseUrls = baseUrls;
        return this;
    }
    public AuthStrategy<IDENTITY> setIdentityType(Class<IDENTITY> identityType) {
        this.identityType = identityType;
        return this;
    }
    public AuthStrategy<IDENTITY> setIdentifier(AuthIdentifier<IDENTITY> identifier) {
        this.identifier = identifier;
        return this;
    }
    public AuthStrategy<IDENTITY> setPermitRequest(Set<SimpleRequest> permitRequest) {
        this.permitRequest = permitRequest;
        return this;
    }
    public AuthStrategy<IDENTITY> setLoginRequest(SimpleRequest loginRequest) {
        this.loginRequest = loginRequest;
        return this;
    }
    public AuthStrategy<IDENTITY> setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
        return this;
    }
    public AuthStrategy<IDENTITY> setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
        return this;
    }
    public AuthStrategy<IDENTITY> setSessionManager(SessionIdentityManager sessionManager) {
        this.sessionManager = sessionManager;
        return this;
    }
    public AuthStrategy<IDENTITY> setRequestMapper(AuthRequestMapper requestMapper) {
        this.requestMapper = requestMapper;
        return this;
    }
    public AuthStrategy<IDENTITY> setListener(AuthEventListener<? super IDENTITY> listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public String toString() {
        List<SimpleKeyValue<String, ?>> kvs = Stream.of(
                new SimpleKeyValue<>("Identity Class", identityType.getName()),
                new SimpleKeyValue<>("Target Base Path(s)", baseUrls),
                new SimpleKeyValue<>("Logout Path", logoutUrl),
                new SimpleKeyValue<>("NoAuth Redirect to", redirectUrl)
        ).collect(Collectors.toList());
        return BoxedTitleMessage.produceLeft(kvs);
    }
}
