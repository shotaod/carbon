package org.carbon.authentication.conf.rule;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
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
import org.carbon.web.translate.dto.Json;

/**
 * @author Shota.Oda 2018/02/12.
 */
public class APIOrientedRule<IDENTITY extends AuthIdentity> extends AbstractDelegateRule<IDENTITY, APIOrientedRule<IDENTITY>> {

    public interface ThrowableMapRequest {
        Optional<AuthRequest> map(HttpServletRequest request) throws Throwable;
    }

    public static class UnAuth implements Json {

        private String message = "unauthorized";

        public String getMessage() {
            return message;
        }
    }

    private static final UnAuth unAuthJson = new UnAuth();

    private DelegateAuthStrategy.MapRequest mapRequest;
    private Set<RequestMatcher> expireTo;

    public APIOrientedRule(Class<IDENTITY> identityClass, AuthDefinitionBuilder parent) {
        super(identityClass, parent);
        expireTo = new HashSet<>();
    }

    @Override
    protected APIOrientedRule<IDENTITY> self() {
        return this;
    }

    public APIOrientedRule<IDENTITY> requestMapping(ThrowableMapRequest mapRequest) {
        this.mapRequest = request -> {
            try {
                return mapRequest.map(request);
            } catch (Throwable throwable) {
                return Optional.empty();
            }
        };
        return self();
    }

    public APIOrientedRule<IDENTITY> expireGet(String... paths) {
        expireTo.addAll(Stream.of(paths).map(path -> new RequestMatcher(HttpMethod.GET, path)).collect(Collectors.toSet()));
        return self();
    }


    @Override
    protected void setupStrategy(DelegateAuthStrategy<IDENTITY> strategy) {
        DelegateAuthStrategy.SignedTranslator translateUnAuth = () -> new SignedTranslatable<>(HttpServletResponse.SC_UNAUTHORIZED, unAuthJson);

        strategy.delegateMapRequest(mapRequest);
        strategy.delegateShouldExpire(request -> expireTo.stream().anyMatch(matcher -> matcher.isMatch(request)));
        strategy.delegateOnProhibitNoAuth(translateUnAuth);
        strategy.delegateOnNoFoundIdentity(translateUnAuth);
    }

    @Override
    public String describe() {
        List<SimpleKeyValue<String, ?>> message = Arrays.asList(
                new SimpleKeyValue<>("Secured Path(s)", _basePaths),
                new SimpleKeyValue<>("Permit Path(s)", _permits.values().stream().flatMap(matcherS -> matcherS.stream().map(RequestMatcher::describe)).collect(Collectors.joining(","))),
                new SimpleKeyValue<>("Auth Path", _authRequestMatcher.describe()),
                new SimpleKeyValue<>("Expire Path", describeExpirePath())
        );
        return String.format("ApiOrientedRule (Rule No. %s)\n", super.id) + BoxedTitleMessage.produceLeft(message);
    }

    private String describeExpirePath() {
        return expireTo.stream().map(RequestMatcher::describe).collect(Collectors.joining(","));
    }
}
