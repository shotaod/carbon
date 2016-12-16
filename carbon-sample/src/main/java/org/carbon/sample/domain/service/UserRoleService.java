package org.carbon.sample.domain.service;

import org.carbon.persistent.annotation.Transactional;
import org.carbon.sample.tables.daos.RoleDao;
import org.carbon.sample.tables.daos.UserDao;
import org.carbon.sample.tables.pojos.Role;
import org.carbon.sample.tables.pojos.User;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Set;

/**
 * @author ubuntu 2016/11/13.
 */
@Component
public class UserRoleService {
	@Inject
	private DSLContext jooq;
	@Inject
	private UserDao userDao;
	@Inject
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
