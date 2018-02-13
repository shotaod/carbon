package org.carbon.authentication.strategy;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.web.context.session.SessionContext;

/**
 * @author Shota Oda 2016/10/29.
 */
@Component
public class AuthStrategyContext {

    @Inject
    private SessionContext session;

    @Inject
    private AuthStrategyRepository repository;

    public AuthStrategyContext(AuthStrategyRepository repository) {
        this.repository = repository;
    }

    public boolean existSecurity() {
        return !repository.isEmpty();
    }

    public Optional<AuthStrategy> findStrategy(HttpServletRequest request, HttpServletResponse response) {
        return repository.find(request, response);
    }
}
