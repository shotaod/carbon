package org.carbon.authentication;

import java.util.List;

/**
 * @author Shota Oda 2016/10/29.
 */
public class AuthStrategyContext {

    private List<AuthStrategy<AuthIdentity>> strategies;

    public AuthStrategyContext(List<AuthStrategy<AuthIdentity>> strategies) {
        this.strategies = strategies;
    }

    public boolean existSecurity() {
        return strategies != null && !strategies.isEmpty();
    }

    public List<AuthStrategy<AuthIdentity>> getStrategies() {
        return strategies;
    }
}
