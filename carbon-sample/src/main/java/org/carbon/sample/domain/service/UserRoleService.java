package org.carbon.sample.domain.service;

import java.util.List;
import java.util.Set;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Assemble;
import org.carbon.persistent.annotation.Transactional;
import org.carbon.sample.ext.jooq.tables.daos.RoleDao;
import org.carbon.sample.ext.jooq.tables.daos.UserDao;
import org.carbon.sample.ext.jooq.tables.pojos.Role;
import org.carbon.sample.ext.jooq.tables.pojos.User;
import org.jooq.DSLContext;

/**
 * @author Shota Oda 2016/11/13.
 */
@Component
public class UserRoleService {
    @Assemble
    private DSLContext jooq;
    @Assemble
    private UserDao userDao;
    @Assemble
    private RoleDao roleDao;

    @Transactional
    public User createUser(User user, Set<Role> roles) {
        roleDao.insert(roles);
        userDao.insert(user);
        return user;
    }

    public User findByUsername(String username) {
        return userDao.fetchOneByUsername(username);
    }

    public List<User> findUsers() {
        return userDao.findAll();
    }
}
