package org.carbon.authentication.conf;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.carbon.authentication.AuthIdentity;
import org.carbon.authentication.conf.rule.APIOrientedRule;
import org.carbon.authentication.conf.rule.ConfigurableRule;
import org.carbon.authentication.conf.rule.PageOrientedRule;
import org.carbon.authentication.conf.rule.Rule;
import org.carbon.authentication.strategy.AuthStrategyRepository;
import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.util.format.ChapterAttr;
import org.carbon.web.context.session.SessionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota Oda 2016/11/03.
 */
@Component
public class AuthDefinitionBuilder {
    private static final Logger logger = LoggerFactory.getLogger(AuthDefinitionBuilder.class);
    private List<Rule> rules;

    @Assemble
    private ObjectMapper objectMapper;

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

    void addRule(Rule rule) {
        rules.add(rule);
    }

    AuthStrategyRepository buildRepository(SessionPool sessionContext) {
        AuthStrategyRepository repo = new AuthStrategyRepository();
        if (logger.isInfoEnabled()) {
            String descriptions = this.rules.stream()
                    .map(Rule::describe)
                    .collect(Collectors.joining("\n"));
            logger.info(ChapterAttr.getBuilder("Authentication Rules").appendLine(descriptions).toString());
        }
        this.rules.stream()
                .map(rule -> rule.convert(sessionContext))
                .forEach(repo::save);
        return repo;
    }
}
