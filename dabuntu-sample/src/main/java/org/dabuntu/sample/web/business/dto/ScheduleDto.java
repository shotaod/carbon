package org.dabuntu.sample.web.business.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.dabunt.sample.tables.pojos.LecturerSchedule;
import org.dabunt.sample.tables.pojos.Student;

/**
 * @author ubuntu 2016/11/28.
 */
@Data
@AllArgsConstructor
public class ScheduleDto {
	private LecturerSchedule lecturerSchedule;
	private Student student;
}
