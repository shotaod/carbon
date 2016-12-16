package org.carbon.sample.web.business.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.carbon.sample.tables.pojos.LecturerSchedule;
import org.carbon.sample.tables.pojos.Student;

/**
 * @author Shota Oda 2016/11/28.
 */
@Data
@AllArgsConstructor
public class ScheduleDto {
	private LecturerSchedule lecturerSchedule;
	private Student student;
}
