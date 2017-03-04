package org.carbon.authentication;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.carbon.util.SimpleKeyValue;
import org.carbon.util.format.BoxedTitleMessage;
import org.carbon.web.def.HttpMethod;

/**
 * @author Shota Oda 2016/10/27.
 */
public abstract class AuthStrategy<IDENTITY extends AuthIdentity> {
    private Class<IDENTITY> identityType;
    private String baseUrl;
    private String logoutUrl;
    private String redirectUrl;
    private AuthSessionManager<IDENTITY> sessionManager;
    private AuthRequestMapper requestMapper;
    private AuthIdentifier<IDENTITY> identifier;
    private AuthEventListener finisher;

    abstract public boolean shouldPermit(HttpMethod method, String requestUrl);
    abstract public boolean shouldTryLogin(HttpMethod method, String requestUrl);

    // -----------------------------------------------------
    //                                               Setter
    //                                               -------
    public void setIdentityType(Class<IDENTITY> identityType) {
        this.identityType = identityType;
    }
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }
    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
    public void setSessionManager(AuthSessionManager<IDENTITY> sessionManager) {
        this.sessionManager = sessionManager;
    }
    public void setRequestMapper(AuthRequestMapper requestMapper) {
        this.requestMapper = requestMapper;
    }
    public void setIdentifier(AuthIdentifier<IDENTITY> identifier) {
        this.identifier = identifier;
    }
    public void setFinisher(AuthEventListener finisher) {
        this.finisher = finisher;
    }

    // -----------------------------------------------------
    //                                               Getter
    //                                               -------
    public Class<IDENTITY> getIdentityType() {
        return identityType;
    }
    public String getBaseUrl() {
        return baseUrl;
    }
    public String getLogoutUrl() {
        return logoutUrl;
    }
    public String getRedirectUrl() {
        return redirectUrl;
    }
    public AuthSessionManager<IDENTITY> getSessionManager() {
        return sessionManager;
    }
    public AuthRequestMapper getRequestMapper() {
        return requestMapper;
    }
    public AuthIdentifier<IDENTITY> getIdentifier() {
        return identifier;
    }
    public AuthEventListener getFinisher() {
        return finisher;
    }

    @Override
    public String toString() {
        List<SimpleKeyValue<String, ?>> kvs = Stream.of(
                new SimpleKeyValue<>("Identity Class", identityType.getName()),
                new SimpleKeyValue<>("Target Base Path", baseUrl),
                new SimpleKeyValue<>("Logout Path", logoutUrl),
                new SimpleKeyValue<>("NoAuth Redirect to", redirectUrl)
        ).collect(Collectors.toList());
        return BoxedTitleMessage.produceLeft(kvs);
    }
}
