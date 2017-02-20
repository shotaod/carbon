package org.carbon.sample.heroku.conf.db;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Configuration;
import org.carbon.component.annotation.Inject;
import org.carbon.sample.heroku.ext.jooq.tables.daos.UserDao;
import org.jooq.DSLContext;
import org.jooq.impl.DAOImpl;

/**
 * @author Shota Oda 2017/02/13.
 */
@Configuration
public class DaoConfiguration {
    @Inject
    private DSLContext jooq;

    @Component
    public UserDao userDao() {
        return construct(UserDao.class);
    }

    private <T extends DAOImpl> T construct(Class<T> daoType) {
        T dao = null;
        try {
            dao = daoType.newInstance();
            dao.setConfiguration(jooq.configuration());
        } catch (InstantiationException | IllegalAccessException ignore) {}
        return dao;
    }
}
