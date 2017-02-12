package org.carbon.sample.domain.service;

import org.carbon.sample.tables.daos.StudentDao;
import org.carbon.sample.tables.pojos.Student;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Optional;

/**
 * @author Shota Oda 2016/11/13.
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
        return Optional.ofNullable(studentDao.fetchOneByEmail(address));
    }

    public List<Student> findStudents() {
        return null;
    }
}
