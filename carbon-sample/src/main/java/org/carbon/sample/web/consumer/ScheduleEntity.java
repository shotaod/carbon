package org.carbon.sample.web.consumer;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.carbon.sample.tables.pojos.Lecturer;
import org.carbon.sample.tables.pojos.LecturerSchedule;

/**
 * @author Shota Oda 2016/11/28.
 */
@Data
@AllArgsConstructor
public class ScheduleEntity {
	private LecturerSchedule schedule;
	private Lecturer lecturer;
}
