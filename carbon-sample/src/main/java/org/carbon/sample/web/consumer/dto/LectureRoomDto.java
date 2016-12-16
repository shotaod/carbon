package org.carbon.sample.web.consumer.dto;

import lombok.Value;
import org.carbon.sample.tables.pojos.Lecturer;
import org.carbon.sample.tables.pojos.LecturerRoom;

import java.util.List;

/**
 * @author Shota Oda 2016/12/10.
 */
@Value
public class LectureRoomDto {
    private Lecturer lecturer;
    private List<LecturerRoom> rooms;
}
