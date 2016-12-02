package org.dabuntu.sample.domain.service;

import org.dabunt.sample.tables.daos.StudentDao;
import org.dabunt.sample.tables.pojos.Student;
import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Inject;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Optional;

/**
 * @author ubuntu 2016/11/13.
 */
@Component
public class StudentService {

	@Inject
	private DSLContext jooq;
	@Inject
	private StudentDao studentDao;

	public Student createStudent(Student student) {
		return null;
	}

	public Optional<Student> selectOneByAddress(String address) {
		return Optional.ofNullable(studentDao.fetchOneByAddress(address));
	}

	public List<Student> findStudents() {
		return null;
	}
}
