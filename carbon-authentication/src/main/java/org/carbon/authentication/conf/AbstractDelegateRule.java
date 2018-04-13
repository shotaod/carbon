package org.carbon.authentication.conf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.carbon.authentication.AuthIdentifier;
import org.carbon.authentication.AuthIdentity;
import org.carbon.authentication.conf.rule.Rule;
import org.carbon.authentication.strategy.AuthStrategy;
import org.carbon.authentication.strategy.delegate.DelegateAuthStrategy;
import org.carbon.authentication.support.RequestMatcher;
import org.carbon.web.context.session.SessionPool;
import org.carbon.web.def.HttpMethod;

/**
 * @author Shota.Oda 2018/02/12.
 */
public abstract class AbstractDelegateRule<IDENTITY extends AuthIdentity, SELF extends AbstractDelegateRule> implements Rule<IDENTITY, SELF> {
    private static Integer COUNTER = 0;
    protected Integer id;
    protected AuthDefinitionBuilder parent;
    protected Class<IDENTITY> identityClass;
    protected List<String> _basePaths;
    protected RequestMatcher _authRequestMatcher;
    protected AuthIdentifier<IDENTITY> _identifier;
    protected Map<HttpMethod, List<RequestMatcher>> _permits;

    public AbstractDelegateRule(Class<IDENTITY> identityClass, AuthDefinitionBuilder parent) {
        this.identityClass = identityClass;
        this.parent = parent;
        this.id = COUNTER++;
    }

    @Override
    public final Class<IDENTITY> identity() {
        return identityClass;
    }

    @Override
    public final AuthDefinitionBuilder end() {
        parent.addRule(this);
        return parent;
    }

    protected abstract SELF self();

    protected abstract void setupStrategy(DelegateAuthStrategy<IDENTITY> strategy);

    // ===================================================================================
    //                                                                          Common Fluent API
    //                                                                          ==========
    @Override
    public SELF base(String... path) {
        _basePaths = Arrays.asList(path);
        return self();
    }

    @Override
    public SELF authTo(HttpMethod method, String path) {
        _authRequestMatcher = new RequestMatcher(method, path);
        return self();
    }

    @Override
    public SELF identifier(AuthIdentifier<IDENTITY> identifier) {
        _identifier = identifier;
        return self();
    }

    public SELF permit(HttpMethod method, String... path) {
        if (_permits == null) {
            _permits = new HashMap<>();
        }
        List<RequestMatcher> matcher = _permits.computeIfAbsent(method, key -> new ArrayList<>());
        matcher.addAll((Stream.of(path).map(p -> new RequestMatcher(method, p)).collect(Collectors.toSet())));
        return self();
    }

    public SELF permitGetAll(String... path) {
        return permit(HttpMethod.GET, path);
    }

    @Override
    public final AuthStrategy convert(SessionPool sessionContext) {
        DelegateAuthStrategy<IDENTITY> strategy = new DelegateAuthStrategy<>(identityClass, sessionContext);

        // common strategies
        strategy.delegateShouldPermitAnonymous(request -> _permits
                .getOrDefault(HttpMethod.codeOf(request.getMethod()), Collections.emptyList())
                .stream()
                .anyMatch(matcher -> matcher.isMatch(request)));
        strategy.delegateFind(authRequest -> _identifier.identify(authRequest.getIdentity()));
        strategy.delegateShouldHandle(request -> _basePaths.stream().anyMatch(base -> {
            String requestPath = request.getPathInfo();
            if (requestPath == null) {
                return base == null;
            }
            return requestPath.startsWith(base);
        }));
        strategy.delegateShouldTryAuth(_authRequestMatcher::isMatch);
        // custom strategy
        setupStrategy(strategy);

        return strategy;
    }
}
