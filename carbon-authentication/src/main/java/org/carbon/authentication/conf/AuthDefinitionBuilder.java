package org.carbon.authentication.conf;

import java.util.ArrayList;
import java.util.List;

import org.carbon.authentication.AuthIdentity;
import org.carbon.authentication.conf.rule.APIOrientedRule;
import org.carbon.authentication.conf.rule.ConfigurableRule;
import org.carbon.authentication.conf.rule.PageOrientedRule;
import org.carbon.authentication.conf.rule.Rule;
import org.carbon.authentication.strategy.AuthStrategyRepository;
import org.carbon.web.context.session.SessionContext;

/**
 * @author Shota Oda 2016/11/03.
 */
public class AuthDefinitionBuilder {
    private List<Rule> rules;

    public AuthDefinitionBuilder() {
        rules = new ArrayList<>();
    }

    public <IDENTITY extends AuthIdentity> ConfigurableRule<IDENTITY> define(Class<IDENTITY> identity) {
        return new ConfigurableRule<>(identity, this);
    }

    public <IDENTITY extends AuthIdentity> PageOrientedRule<IDENTITY> defineForPage(Class<IDENTITY> identity) {
        return new PageOrientedRule<>(identity, this);
    }

    public <IDENTITY extends AuthIdentity> APIOrientedRule<IDENTITY> defineForAPI(Class<IDENTITY> identity) {
        return new APIOrientedRule<>(identity, this);
    }

    AuthStrategyRepository buildRepository(SessionContext sessionContext) {
        AuthStrategyRepository repo = new AuthStrategyRepository();
        this.rules.stream()
                .map(rule -> rule.convert(sessionContext))
                .forEach(repo::save);
        return repo;
    }
}
