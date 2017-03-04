package org.carbon.sample.web.business;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;

import lombok.Data;
import org.carbon.sample.ext.jooq.tables.pojos.LecturerRoom;

/**
 * @author Shota Oda 2016/11/28.
 */
@Data
public class RoomCreateForm {
    @NotNull
    private String roomName;
    @NotNull
    private String roomDetail;
    @NotNull
    private LocalDateTime beginDatetime;
    @NotNull
    private LocalDateTime endDatetime;

    public LecturerRoom toEntity(Long lecturerId) {
        LecturerRoom entity = new org.carbon.sample.ext.jooq.tables.pojos.LecturerRoom();
        entity.setLecturerId(lecturerId);
        entity.setRoomName(roomName);
        entity.setRoomDetail(roomDetail);
        entity.setBeginDatetime(beginDatetime);
        entity.setEndDatetime(endDatetime);
        return entity;
    }
}
