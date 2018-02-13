package org.carbon.authentication.conf.rule;

import org.carbon.authentication.AuthIdentifier;
import org.carbon.authentication.AuthIdentity;
import org.carbon.authentication.conf.AbstractDelegateRule;
import org.carbon.authentication.conf.AuthDefinitionBuilder;
import org.carbon.authentication.strategy.AuthStrategy;
import org.carbon.authentication.strategy.delegate.DelegateAuthStrategy;
import org.carbon.web.context.session.SessionContext;
import org.carbon.web.def.HttpMethod;

/**
 * @author garden 2018/02/12.
 */
public class ConfigurableRule<IDENTITY extends AuthIdentity> extends AbstractDelegateRule<IDENTITY, ConfigurableRule> {
    public ConfigurableRule(Class<IDENTITY> identityClass, AuthDefinitionBuilder parent) {
        super(identityClass, parent);
    }

    @Override
    public ConfigurableRule base(String... path) {
        return null;
    }

    @Override
    public ConfigurableRule authTo(HttpMethod method, String path) {
        return this;
    }

    @Override
    public ConfigurableRule identifier(AuthIdentifier<IDENTITY> identifier) {
        return null;
    }

    @Override
    public AuthStrategy convert(SessionContext sessionContext) {
        return null;
    }
}
