package org.carbon.sample.domain.service;

import org.carbon.persistent.annotation.Transactional;
import org.carbon.sample.tables.daos.LecturerDao;
import org.carbon.sample.tables.pojos.Lecturer;
import org.carbon.sample.tables.records.LecturerRecord;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.jooq.DSLContext;

import java.util.List;

/**
 * @author Shota Oda 2016/11/13.
 */
@Component
public class LecturerService {
	@Inject
	private DSLContext jooq;
	@Inject
	private LecturerDao lecturerDao;

	@Transactional
	public LecturerRecord createLecturer(LecturerRecord record) {
		return record;
	}

	public Lecturer findByAddress(String address) {
		return lecturerDao.fetchOneByEmail(address);
	}

	public List<Lecturer> findLecturers() {
		return null;
	}
}
