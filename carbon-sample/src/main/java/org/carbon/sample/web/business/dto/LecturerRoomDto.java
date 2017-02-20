package org.carbon.sample.web.business.dto;

import lombok.Value;
import org.carbon.sample.ext.jooq.tables.pojos.Lecturer;
import org.carbon.sample.ext.jooq.tables.pojos.LecturerRoom;

import java.util.List;

/**
 * @author Shota Oda 2016/12/10.
 */
@Value
public class LecturerRoomDto {
    private Lecturer lecturer;
    private List<LecturerRoom> rooms;
}
