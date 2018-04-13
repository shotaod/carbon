package org.carbon.sample.web.business;

import java.util.ArrayList;
import java.util.List;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Assemble;
import org.carbon.persistent.annotation.Transactional;
import org.carbon.sample.ext.jooq.Tables;
import org.carbon.sample.ext.jooq.tables.daos.LecturerDao;
import org.carbon.sample.ext.jooq.tables.daos.LecturerRoomDao;
import org.carbon.sample.ext.jooq.tables.daos.LecturerScheduleDao;
import org.carbon.sample.ext.jooq.tables.pojos.Lecturer;
import org.carbon.sample.ext.jooq.tables.pojos.LecturerRoom;
import org.carbon.sample.ext.jooq.tables.pojos.LecturerSchedule;
import org.carbon.sample.ext.jooq.tables.pojos.Student;
import org.carbon.sample.web.business.dto.LecturerRoomDto;
import org.carbon.sample.web.business.dto.ScheduleDto;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;

/**
 * @author Shota Oda 2016/11/28.
 */
@Component
public class LecturerAppService {
    @Assemble
    private DSLContext jooq;
    @Assemble
    private LecturerDao lecturerDao;
    @Assemble
    private LecturerScheduleDao lecturerScheduleDao;
    @Assemble
    private LecturerRoomDao lecturerRoomDao;


    public List<ScheduleDto> selectSchedules(Long lecturerId) {
        return jooq.select()
                .from(Tables.LECTURER_SCHEDULE)
                .leftJoin(Tables.STUDENT).onKey()
                .where(Tables.LECTURER_SCHEDULE.LECTURER_ID.eq(lecturerId)).fetch()
                .map(record -> {
                    LecturerSchedule schedule = record.into(Tables.LECTURER_SCHEDULE).into(LecturerSchedule.class);
                    Student student = record.into(Tables.STUDENT).into(Student.class);
                    return new ScheduleDto(schedule, student);
                });
    }

    public LecturerRoomDto selectRooms(Long lecturerId) {
        Result<Record> records = jooq.select()
                .from(Tables.LECTURER.leftJoin(Tables.LECTURER_ROOM).onKey())
                .where(Tables.LECTURER.ID.eq(lecturerId))
                .fetch();
        Lecturer lecturer = records.into(Lecturer.class).get(0);
        List<LecturerRoom> rooms = records.into(LecturerRoom.class);
        if (rooms.get(0).getId() == null) {
            rooms = new ArrayList<>();
        }
        return new LecturerRoomDto(lecturer, rooms);
    }

    public Lecturer selectLecturer(Long lecturerId) {
        return lecturerDao.findById(lecturerId);
    }

    @Transactional
    public Lecturer updateProfile(LecturerProfileForm form, Long lecturerId) {
        Lecturer lecturer = form.toEntity();
        lecturer.setId(lecturerId);
        lecturerDao.update(lecturer);
        return lecturerDao.findById(lecturerId);
    }

    @Transactional
    public void insertSchedule(ScheduleForm form, Long lecturerId) {
        LecturerSchedule entity = form.toEntity(lecturerId);
        lecturerScheduleDao.insert(entity);
    }

    @Transactional
    public void insertRoom(RoomCreateForm form, Long lecturerId) {
        LecturerRoom entity = form.toEntity(lecturerId);
        lecturerRoomDao.insert(entity);

    }
}
