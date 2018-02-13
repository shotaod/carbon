package org.carbon.authentication.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Shota Oda 2016/10/27.
 */
public class AuthStrategyRepository {
    private List<AuthStrategy> strategies;

    public AuthStrategyRepository() {
        strategies = new ArrayList<>();
    }

    public void save(AuthStrategy strategy) {
        strategies.add(strategy);
    }

    public Optional<AuthStrategy> find(HttpServletRequest request, HttpServletResponse response) {
        for (AuthStrategy strategy : strategies) {
            if (strategy.shouldHandle(request)) {
                return Optional.of(strategy.prototype(request, response));
            }
        }
        return Optional.empty();
    }

    public List<AuthStrategy> findAll() {
        return strategies;
    }

    public boolean isEmpty() {
        return strategies.isEmpty();
    }
}
