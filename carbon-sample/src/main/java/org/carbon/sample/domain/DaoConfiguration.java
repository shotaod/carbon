package org.carbon.sample.domain;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Configuration;
import org.carbon.component.annotation.Inject;
import org.carbon.sample.ext.jooq.tables.daos.LecturerApplyHistoryDao;
import org.carbon.sample.ext.jooq.tables.daos.LecturerDao;
import org.carbon.sample.ext.jooq.tables.daos.LecturerRoomDao;
import org.carbon.sample.ext.jooq.tables.daos.LecturerScheduleDao;
import org.carbon.sample.ext.jooq.tables.daos.ProductDao;
import org.carbon.sample.ext.jooq.tables.daos.RoleDao;
import org.carbon.sample.ext.jooq.tables.daos.StudentDao;
import org.carbon.sample.ext.jooq.tables.daos.UserDao;
import org.jooq.DSLContext;
import org.jooq.impl.DAOImpl;

/**
 * @author Shota Oda 2016/11/26.
 */
@Configuration
public class DaoConfiguration {
    @Inject
    private DSLContext jooq;

    @Component
    public LecturerApplyHistoryDao lectureapplyhistoryDao() {
        return construct(LecturerApplyHistoryDao.class);
    }

    @Component
    public LecturerDao lecturerDao() {
        return construct(LecturerDao.class);
    }

    @Component
    public LecturerScheduleDao lecturerscheduleDao() {
        return construct(LecturerScheduleDao.class);
    }

    @Component
    public LecturerRoomDao lecturerRoomDao() {
        return construct(LecturerRoomDao.class);
    }

    @Component
    public ProductDao productDao() {
        return construct(ProductDao.class);
    }

    @Component
    public RoleDao roleDao() {
        return construct(RoleDao.class);
    }

    @Component
    public StudentDao studentDao() {
        return construct(StudentDao.class);
    }

    @Component
    public UserDao userDao() {
        return construct(UserDao.class);
    }

    private <T extends DAOImpl> T construct(Class<T> daoType) {
        T dao = null;
        try {
            dao = daoType.newInstance();
            dao.setConfiguration(jooq.configuration());
        } catch (InstantiationException | IllegalAccessException ignore) {
        }
        return dao;
    }
}
