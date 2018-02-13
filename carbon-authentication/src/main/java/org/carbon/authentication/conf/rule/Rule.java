package org.carbon.authentication.conf.rule;

import org.carbon.authentication.AuthIdentifier;
import org.carbon.authentication.AuthIdentity;
import org.carbon.authentication.conf.AuthDefinitionBuilder;
import org.carbon.authentication.strategy.AuthStrategy;
import org.carbon.web.context.session.SessionContext;
import org.carbon.web.def.HttpMethod;

/**
 * @author garden 2018/02/12.
 */
public interface Rule<IDENTITY extends AuthIdentity, SELF extends Rule> {
    Class<IDENTITY> identity();

    SELF base(String... path);

    SELF authTo(HttpMethod method, String path);

    SELF identifier(AuthIdentifier<IDENTITY> identifier);

    AuthDefinitionBuilder end();

    AuthStrategy convert(SessionContext sessionContext);
}
