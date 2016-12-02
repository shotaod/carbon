package org.dabuntu.sample.web.business;

import org.dabunt.persistent.annotation.Transactional;
import org.dabunt.sample.Tables;
import org.dabunt.sample.tables.daos.LecturerScheduleDao;
import org.dabunt.sample.tables.pojos.LecturerSchedule;
import org.dabunt.sample.tables.pojos.Student;
import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Inject;
import org.jooq.DSLContext;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author ubuntu 2016/11/28.
 */
@Component
public class LecturerAppService {
	@Inject
	private DSLContext jooq;
	@Inject
	private LecturerScheduleDao lecturerScheduleDao;

	public List<ScheduleEntity> selectSchedules(Long lecturerId) {
		return jooq.select()
			.from(Tables.LECTURER_SCHEDULE)
			.leftJoin(Tables.STUDENT).onKey()
			.where(Tables.LECTURER_SCHEDULE.LECTURER_ID.eq(lecturerId)).fetch()
			.map(record -> {
				LecturerSchedule schedule = record.into(Tables.LECTURER_SCHEDULE).into(LecturerSchedule.class);
				Student student = record.into(Tables.STUDENT).into(Student.class);
				return new ScheduleEntity(schedule, student);
			});
	}

	@Transactional
	public void insertSchedule(ScheduleForm form, Long lecturerId) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

		LocalDateTime begin = LocalDateTime.parse(form.getBeginDatetime(), formatter);
		LocalDateTime end = LocalDateTime.parse(form.getEndDatetime(), formatter);
		LecturerSchedule schedule = new LecturerSchedule(null, Timestamp.valueOf(begin), Timestamp.valueOf(end), lecturerId, null);
		lecturerScheduleDao.insert(schedule);
	}
}
