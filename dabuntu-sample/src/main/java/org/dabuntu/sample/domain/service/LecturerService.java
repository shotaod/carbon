package org.dabuntu.sample.domain.service;

import org.dabunt.persistent.annotation.Transactional;
import org.dabunt.sample.tables.daos.LecturerDao;
import org.dabunt.sample.tables.pojos.Lecturer;
import org.dabunt.sample.tables.records.LecturerRecord;
import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Inject;
import org.jooq.DSLContext;

import java.util.List;

/**
 * @author ubuntu 2016/11/13.
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
		return lecturerDao.fetchOneByAddress(address);
	}

	public List<Lecturer> findLecturers() {
		return null;
	}
}
