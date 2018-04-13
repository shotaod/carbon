package org.carbon.authentication.conf.rule;

import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import org.carbon.authentication.AuthIdentity;
import org.carbon.authentication.conf.AbstractDelegateRule;
import org.carbon.authentication.conf.AuthDefinitionBuilder;
import org.carbon.authentication.strategy.delegate.DelegateAuthStrategy;
import org.carbon.authentication.strategy.request.AuthRequest;
import org.carbon.authentication.support.RequestMatcher;
import org.carbon.authentication.translator.SignedTranslatable;
import org.carbon.util.SimpleKeyValue;
import org.carbon.util.format.BoxedTitleMessage;
import org.carbon.web.def.HttpMethod;
import org.carbon.web.translate.dto.NoBody;
import org.carbon.web.translate.dto.Redirect;

/**
 * @author Shota.Oda 2018/02/12.
 */
public class PageOrientedRule<IDENTITY extends AuthIdentity> extends AbstractDelegateRule<IDENTITY, PageOrientedRule<IDENTITY>> {

    private enum AuthType {
        Basic,
        Form,
    }

    private static class BasicUnauthorized implements NoBody {
        @Override
        public Map<String, String> headers() {
            Map<String, String> headers = new HashMap<>();
            headers.put(Header_Auth, "Basic realm=Basic");
            return headers;
        }
    }

    private static final String Header_Auth = "Authorization";
    private static final String Auth_Basic = "Basic";
    private static final BasicUnauthorized BASIC_UNAUTHORIZED = new BasicUnauthorized();

    private AuthType authType = AuthType.Form;
    private RequestMatcher _logoutRequest;
    private String _redirectPath;
    private String _requestIdentityKey;
    private String _requestSecretKey;

    public PageOrientedRule(Class<IDENTITY> identityClass, AuthDefinitionBuilder parent) {
        super(identityClass, parent);
    }

    @Override
    protected PageOrientedRule<IDENTITY> self() {
        return this;
    }

    public PageOrientedRule<IDENTITY> logout(String path) {
        _logoutRequest = new RequestMatcher(HttpMethod.GET, path);
        return this;
    }

    public PageOrientedRule<IDENTITY> redirect(String path) {
        _redirectPath = path;
        return this;
    }

    public PageOrientedRule<IDENTITY> authKey(String usernameOrEmail, String password) {
        _requestIdentityKey = usernameOrEmail;
        _requestSecretKey = password;
        return this;
    }

    public PageOrientedRule<IDENTITY> basicAuth() {
        authType = AuthType.Basic;
        return this;
    }

    @Override
    protected void setupStrategy(DelegateAuthStrategy<IDENTITY> strategy) {
        strategy.delegateShouldExpire(_logoutRequest::isMatch);
        switch (authType) {
            case Basic:
                setupBasicAuth(strategy);
            case Form:
            default:
                setupFormAuth(strategy);
        }
    }

    // -----------------------------------------------------
    //                                               Form Auth
    //                                               -------
    private void setupFormAuth(DelegateAuthStrategy<IDENTITY> strategy) {
        strategy.delegateMapRequest(request -> {
            String identity = request.getParameter(_requestIdentityKey);
            String secret = request.getParameter(_requestSecretKey);
            if (identity == null || secret == null) {
                return Optional.empty();
            }
            return Optional.of(new AuthRequest(identity, secret));
        });
        DelegateAuthStrategy.SignedTranslator onFail = () -> new SignedTranslatable<>(HttpServletResponse.SC_FOUND, new Redirect(_redirectPath));
        strategy.delegateOnExpire(onFail);
        strategy.delegateOnIllegalAuthRequest(onFail);
        strategy.delegateOnNoFoundIdentity(onFail);
        strategy.delegateOnNoMatchSecret(onFail);
        strategy.delegateOnProhibitNoAuth(onFail);
    }

    // -----------------------------------------------------
    //                                               Basic Auth
    //                                               -------
    private void setupBasicAuth(DelegateAuthStrategy<IDENTITY> strategy) {
        strategy.delegateMapRequest(request -> Optional.ofNullable(request.getHeader(Header_Auth))
                .filter(header -> header.startsWith(Auth_Basic))
                .flatMap(header -> {
                    String base64 = header.replace(Auth_Basic, "").trim();
                    String[] info = new String(Base64.getDecoder().decode(base64)).split(":");
                    if (info.length != 2) {
                        return Optional.<AuthRequest>empty();
                    }
                    return Optional.of(new AuthRequest(info[0], info[1]));
                }));

        DelegateAuthStrategy.SignedTranslator onFail = () -> new SignedTranslatable<>(HttpServletResponse.SC_UNAUTHORIZED, BASIC_UNAUTHORIZED);

        strategy.delegateOnExpire(onFail);
        strategy.delegateOnIllegalAuthRequest(onFail);
        strategy.delegateOnNoFoundIdentity(onFail);
        strategy.delegateOnNoMatchSecret(onFail);
        strategy.delegateOnProhibitNoAuth(onFail);
    }

    @Override
    public String describe() {
        List<SimpleKeyValue<String, ?>> message = Arrays.asList(
                new SimpleKeyValue<>("Auth Type", authType.name()),
                new SimpleKeyValue<>("Secured Path(s)", _basePaths),
                new SimpleKeyValue<>("Permit Path(s)", _permits.values().stream().flatMap(matcherS -> matcherS.stream().map(RequestMatcher::describe)).collect(Collectors.joining(","))),
                new SimpleKeyValue<>("Login Path", authType == AuthType.Basic ? "{secured_path}/**" : _authRequestMatcher.describe()),
                new SimpleKeyValue<>("Logout Path", _logoutRequest.describe()),
                new SimpleKeyValue<>("Anonymous Redirect Path", _redirectPath),
                new SimpleKeyValue<>("Request ID Key", _requestIdentityKey),
                new SimpleKeyValue<>("Request Pass Key", _requestSecretKey)
        );
        return String.format("PageOrientedRule (Rule No. %s)\n", super.id) + BoxedTitleMessage.produceLeft(message);
    }
}
