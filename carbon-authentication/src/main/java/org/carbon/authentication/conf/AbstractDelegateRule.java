package org.carbon.authentication.conf;

import org.carbon.authentication.AuthIdentity;
import org.carbon.authentication.conf.rule.Rule;

/**
 * @author garden 2018/02/12.
 */
public abstract class AbstractDelegateRule<IDENTITY extends AuthIdentity, SELF extends AbstractDelegateRule> implements Rule<IDENTITY, SELF> {
    protected Class<IDENTITY> identityClass;
    protected AuthDefinitionBuilder parent;

    public AbstractDelegateRule(Class<IDENTITY> identityClass, AuthDefinitionBuilder parent) {
        this.identityClass = identityClass;
        this.parent = parent;
    }

    @Override
    public Class<IDENTITY> identity() {
        return identityClass;
    }

    @Override
    public AuthDefinitionBuilder end() {
        return parent;
    }
}
