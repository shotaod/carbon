package org.carbon.sample.domain.service;

import java.util.List;
import java.util.Optional;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Assemble;
import org.carbon.sample.ext.jooq.tables.daos.StudentDao;
import org.carbon.sample.ext.jooq.tables.pojos.Student;
import org.jooq.DSLContext;

/**
 * @author Shota Oda 2016/11/13.
 */
@Component
public class StudentService {

    @Assemble
    private DSLContext jooq;
    @Assemble
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
