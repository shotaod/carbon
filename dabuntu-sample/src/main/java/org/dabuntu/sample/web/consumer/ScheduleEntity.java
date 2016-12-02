package org.dabuntu.sample.web.consumer;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.dabunt.sample.tables.pojos.Lecturer;
import org.dabunt.sample.tables.pojos.LecturerSchedule;

/**
 * @author ubuntu 2016/11/28.
 */
@Data
@AllArgsConstructor
public class ScheduleEntity {
	private LecturerSchedule schedule;
	private Lecturer lecturer;
}
