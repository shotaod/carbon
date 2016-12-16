package org.carbon.sample.web.business;

import org.carbon.persistent.annotation.Transactional;
import org.carbon.sample.web.business.dto.ScheduleDto;
import org.carbon.sample.Tables;
import org.carbon.sample.tables.daos.LecturerDao;
import org.carbon.sample.tables.daos.LecturerRoomDao;
import org.carbon.sample.tables.daos.LecturerScheduleDao;
import org.carbon.sample.tables.pojos.Lecturer;
import org.carbon.sample.tables.pojos.LecturerRoom;
import org.carbon.sample.tables.pojos.LecturerSchedule;
import org.carbon.sample.tables.pojos.Student;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.sample.web.business.dto.LecturerRoomDto;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;

import java.util.List;

/**
 * @author Shota Oda 2016/11/28.
 */
@Component
public class LecturerAppService {
	@Inject
	private DSLContext jooq;
    @Inject
    private LecturerDao lecturerDao;
	@Inject
	private LecturerScheduleDao lecturerScheduleDao;
    @Inject
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
				.from(Tables.LECTURER.join(Tables.LECTURER_ROOM).onKey())
				.where(Tables.LECTURER.ID.eq(lecturerId))
				.fetch();
		Lecturer lecturer = records.into(Lecturer.class).get(0);
		List<LecturerRoom> rooms = records.into(LecturerRoom.class);
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
