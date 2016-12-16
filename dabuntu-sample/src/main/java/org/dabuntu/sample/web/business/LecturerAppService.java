package org.dabuntu.sample.web.business;

import org.dabunt.persistent.annotation.Transactional;
import org.dabunt.sample.Tables;
import org.dabunt.sample.tables.daos.LecturerDao;
import org.dabunt.sample.tables.daos.LecturerRoomDao;
import org.dabunt.sample.tables.daos.LecturerScheduleDao;
import org.dabunt.sample.tables.pojos.Lecturer;
import org.dabunt.sample.tables.pojos.LecturerRoom;
import org.dabunt.sample.tables.pojos.LecturerSchedule;
import org.dabunt.sample.tables.pojos.Student;
import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Inject;
import org.dabuntu.sample.web.business.dto.LecturerRoomDto;
import org.dabuntu.sample.web.business.dto.ScheduleDto;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;

import java.util.List;

/**
 * @author ubuntu 2016/11/28.
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
