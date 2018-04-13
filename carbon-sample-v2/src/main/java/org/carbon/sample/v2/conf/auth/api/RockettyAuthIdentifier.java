package org.carbon.sample.v2.conf.auth.api;

import java.util.Optional;

import org.carbon.authentication.AuthIdentifier;
import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.sample.v2.ext.jooq.tables.daos.RockettyAuthClientDao;

/**
 * @author Shota.Oda 2018/02/15.
 */
@Component
public class RockettyAuthIdentifier implements AuthIdentifier<RockettyClientIdentity> {

    @Assemble
    private RockettyAuthClientDao dao;

    @Override
    public Optional<RockettyClientIdentity> identify(String identity) {
        return Optional
                .ofNullable(dao.fetchOneByClientId(identity))
                .map(data -> data.getValid() ? data : null)
                .map(RockettyClientIdentity::new);
    }
}
