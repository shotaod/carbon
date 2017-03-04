package org.carbon.sample.web.business;

import java.time.LocalDateTime;

import lombok.Data;
import org.carbon.sample.ext.jooq.tables.pojos.LecturerSchedule;

/**
 * @author Shota Oda 2016/11/28.
 */
@Data
public class ScheduleForm {
    private LocalDateTime beginDatetime;
    private LocalDateTime endDatetime;

    public LecturerSchedule toEntity(Long lecturerId) {
        LecturerSchedule entity = new LecturerSchedule();
        entity.setLecturerId(lecturerId);
        entity.setBeginDatetime(beginDatetime);
        entity.setEndDatetime(endDatetime);
        return entity;
    }
}
