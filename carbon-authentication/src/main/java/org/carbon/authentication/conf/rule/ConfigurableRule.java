package org.carbon.authentication.conf.rule;

import org.carbon.authentication.AuthIdentity;
import org.carbon.authentication.conf.AbstractDelegateRule;
import org.carbon.authentication.conf.AuthDefinitionBuilder;
import org.carbon.authentication.strategy.delegate.DelegateAuthStrategy;

/**
 * @author Shota.Oda 2018/02/12.
 */
public class ConfigurableRule<IDENTITY extends AuthIdentity> extends AbstractDelegateRule<IDENTITY, ConfigurableRule> {
    public ConfigurableRule(Class<IDENTITY> identityClass, AuthDefinitionBuilder parent) {
        super(identityClass, parent);
    }

    @Override
    protected ConfigurableRule self() {
        return this;
    }

    @Override
    protected void setupStrategy(DelegateAuthStrategy<IDENTITY> strategy) {
        // todo implement
    }

    @Override
    public String describe() {
        return null;
    }
}
