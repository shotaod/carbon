package org.carbon.sample.domain.service;

import java.util.List;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Assemble;
import org.carbon.persistent.annotation.Transactional;
import org.carbon.sample.ext.jooq.tables.daos.LecturerDao;
import org.carbon.sample.ext.jooq.tables.pojos.Lecturer;
import org.carbon.sample.ext.jooq.tables.records.LecturerRecord;
import org.jooq.DSLContext;

/**
 * @author Shota Oda 2016/11/13.
 */
@Component
public class LecturerService {
    @Assemble
    private DSLContext jooq;
    @Assemble
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
