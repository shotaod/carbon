package org.carbon.authentication.conf.rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import org.carbon.authentication.AuthIdentifier;
import org.carbon.authentication.AuthIdentity;
import org.carbon.authentication.conf.AbstractDelegateRule;
import org.carbon.authentication.conf.AuthDefinitionBuilder;
import org.carbon.authentication.strategy.AuthStrategy;
import org.carbon.authentication.strategy.delegate.DelegateAuthStrategy;
import org.carbon.authentication.strategy.request.AuthRequest;
import org.carbon.authentication.support.RequestMather;
import org.carbon.web.context.session.SessionContext;
import org.carbon.web.def.HttpMethod;
import org.carbon.web.util.ResponseUtil;

/**
 * @author garden 2018/02/12.
 */
public class PageOrientedRule<IDENTITY extends AuthIdentity> extends AbstractDelegateRule<IDENTITY, PageOrientedRule> {
    private static final String Header_Auth = "Authorization";
    private static final String Auth_Basic = "Basic";

    private List<String> _basePaths;
    private RequestMather _endPointRequest;
    private RequestMather _logoutRequest;
    private String _redirectPath;
    private Map<String, List<RequestMather>> permits;
    private String _requestIdentityKey;
    private String _requestSecretKey;
    private AuthIdentifier<AuthIdentity> _identifier;

    public PageOrientedRule(Class<IDENTITY> identityClass, AuthDefinitionBuilder parent) {
        super(identityClass, parent);
    }

    @Override
    public PageOrientedRule<IDENTITY> base(String... path) {
        _basePaths = Arrays.asList(path);
        return this;
    }

    @Override
    public PageOrientedRule<IDENTITY> authTo(HttpMethod method, String path) {
        _endPointRequest = new RequestMather(method, path);
        return this;
    }

    @Override
    public PageOrientedRule identifier(AuthIdentifier<IDENTITY> identifier) {
        _identifier = (AuthIdentifier<AuthIdentity>) identifier;
        return this;
    }

    public PageOrientedRule<IDENTITY> logout(String path) {
        _logoutRequest = new RequestMather(HttpMethod.GET, path);
        return this;
    }

    public PageOrientedRule<IDENTITY> redirect(String path) {
        _redirectPath = path;
        return this;
    }

    public PageOrientedRule<IDENTITY> permit(HttpMethod method, String... path) {
        if (permits == null) {
            permits = new HashMap<>();
        }
        List<RequestMather> paths = permits.getOrDefault(method.getCode(), new ArrayList<>());
        paths.addAll(Stream.of(path).map(p -> new RequestMather(method, p)).collect(Collectors.toSet()));
        return this;
    }

    public PageOrientedRule<IDENTITY> permitGetAll(String... path) {
        permit(HttpMethod.GET, path);
        return this;
    }

    public PageOrientedRule<IDENTITY> requestKey(String usernameOrEmail, String password) {
        _requestIdentityKey = usernameOrEmail;
        _requestSecretKey = password;
        return this;
    }

    public PageOrientedRule<IDENTITY> basicAuth() {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader(Header_Auth, "Basic realm=Basic");
        return Optional.ofNullable(request.getHeader(Header_Auth))
                .filter(header -> header.startsWith(Auth_Basic))
                .flatMap(header -> {
                    String base64 = header.replace(Auth_Basic, "").trim();
                    String[] info = new String(Base64.getDecoder().decode(base64)).split(":");
                    if (info.length != 2) {
                        return Optional.empty();
                    }
                    return Optional.of(new AuthInfo(info[0], info[1]));
                });
    }

    @Override
    public AuthStrategy convert(SessionContext sessionContext) {
        DelegateAuthStrategy<IDENTITY> strategy = new DelegateAuthStrategy<>(identityClass, sessionContext);
        strategy.delegateShouldHandle(request -> _basePaths.stream().anyMatch(base -> {
            String requestPath = request.getPathInfo();
            if (requestPath == null) {
                return base == null;
            }
            return requestPath.startsWith(base);
        }));
        strategy.delegateShouldPermitNoAuth(request -> permits
                .get(request.getMethod())
                .stream()
                .anyMatch(matcher -> matcher.isMatch(request)));
        strategy.delegateShouldTryAuth(_endPointRequest::isMatch);
        strategy.delegateShouldExpire(_logoutRequest::isMatch);
        strategy.delegateMapRequest(request -> {
            String identity = request.getParameter(_requestIdentityKey);
            String secret = request.getParameter(_requestSecretKey);
            if (identity == null || secret == null) {
                return Optional.empty();
            }
            return Optional.of(new AuthRequest(identity, secret));
        });
        strategy.delegateFind(authRequest -> _identifier.find(authRequest.getIdentity()));
        strategy.delegateOnExpire(response -> ResponseUtil.redirect(response, _redirectPath));
        strategy.delegateOnExistSession(response -> ResponseUtil.redirect(response, _redirectPath));
        strategy.delegateOnIllegalAuthRequest(response -> ResponseUtil.redirect(response, _redirectPath));
        strategy.delegateOnNoFoundIdentity(response -> ResponseUtil.redirect(response, _redirectPath));
        strategy.delegateOnNoMatchSecret(response -> ResponseUtil.redirect(response, _redirectPath));
        strategy.delegateOnProhibitNoAuth(response -> ResponseUtil.redirect(response, _redirectPath));
        return strategy;
    }
}
