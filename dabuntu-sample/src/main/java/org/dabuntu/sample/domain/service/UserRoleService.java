package org.dabuntu.sample.domain.service;

import org.dabunt.persistent.annotation.Transactional;
import org.dabunt.sample.tables.daos.RoleDao;
import org.dabunt.sample.tables.daos.UserDao;
import org.dabunt.sample.tables.pojos.Role;
import org.dabunt.sample.tables.pojos.User;
import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Inject;
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
		return userDao.fetchOneByUserName(username);
	}

	public List<User> findUsers() {
		return userDao.findAll();
	}
}
