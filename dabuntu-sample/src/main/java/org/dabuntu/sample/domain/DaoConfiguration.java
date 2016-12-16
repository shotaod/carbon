package org.dabuntu.sample.domain;

import org.dabunt.sample.tables.daos.*;
import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Configuration;
import org.dabuntu.component.annotation.Inject;
import org.jooq.DSLContext;
import org.jooq.impl.DAOImpl;

/**
 * @author ubuntu 2016/11/26.
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
		} catch (InstantiationException | IllegalAccessException ignore) {}
		return dao;
	}
}
