package org.dabuntu.sample.web.consumer;

import org.dabunt.persistent.annotation.Transactional;
import org.dabunt.sample.Tables;
import org.dabunt.sample.tables.daos.LecturerApplyHistoryDao;
import org.dabunt.sample.tables.daos.LecturerScheduleDao;
import org.dabunt.sample.tables.pojos.LecturerApplyHistory;
import org.dabunt.sample.tables.pojos.LecturerSchedule;
import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Inject;
import org.jooq.DSLContext;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ubuntu 2016/11/27.
 */
@Component
public class StudentAppService {
	@Inject
	private DSLContext jooq;
	@Inject
	private LecturerScheduleDao lecturerScheduleDao;
	@Inject
	private LecturerApplyHistoryDao lecturerApplyHistoryDao;

	@Transactional
	public void apply(Long id, Long lecturerId, Long studentId) {
		LecturerApplyHistory applyHistory = new LecturerApplyHistory(null, lecturerId, id, studentId);
		lecturerApplyHistoryDao.insert(applyHistory);
	}

	public List<LecturerSchedule> selectLecturerSchedule() {
		return jooq.select()
			.from(Tables.LECTURER_SCHEDULE)
			.where(Tables.LECTURER_SCHEDULE.BEGIN_DATETIME.gt(Timestamp.valueOf(LocalDateTime.now())))
			.fetch()
			.into(Tables.LECTURER_SCHEDULE)
			.into(LecturerSchedule.class);
	}
}
