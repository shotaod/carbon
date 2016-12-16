package org.dabuntu.sample.web.consumer;

import lombok.NonNull;
import org.dabunt.persistent.annotation.Transactional;
import org.dabunt.sample.Tables;
import org.dabunt.sample.tables.daos.LecturerApplyHistoryDao;
import org.dabunt.sample.tables.daos.LecturerDao;
import org.dabunt.sample.tables.daos.LecturerScheduleDao;
import org.dabunt.sample.tables.pojos.*;
import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Inject;
import org.dabuntu.sample.web.consumer.dto.LectureRoomDto;
import org.dabuntu.sample.web.consumer.dto.LecturerIndex;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ubuntu 2016/11/27.
 */
@Component
public class StudentAppService {
    @Inject
    private DSLContext jooq;
    @Inject
    private LecturerDao lecturerDao;
    @Inject
    private LecturerScheduleDao lecturerScheduleDao;
    @Inject
    private LecturerApplyHistoryDao lecturerApplyHistoryDao;

    @Transactional
    public void apply(Long id, Long lecturerId, Long studentId) {
        LecturerApplyHistory applyHistory = new LecturerApplyHistory((Long) null);
        lecturerApplyHistoryDao.insert(applyHistory);
    }

    public List<LecturerIndex> selectLecturers() {
        return jooq.select()
                .from(Tables.LECTURER.join(Tables.ASSET).onKey())
                .fetch()
                .stream()
                .map(record -> {
                    Lecturer lecturer = record.into(Lecturer.class);
                    Asset asset = record.into(Asset.class);
                    return new LecturerIndex(lecturer, asset);
                }).collect(Collectors.toList());
    }

    public LectureRoomDto selectLecturerRoom(@NonNull Long lecturerId) {
        Result<Record> records = jooq.select()
                .from(Tables.LECTURER.leftJoin(Tables.LECTURER_ROOM).onKey())
                .where(Tables.LECTURER.ID.eq(lecturerId))
                .fetch();
        Lecturer lecturer = records.into(Lecturer.class).get(0);
        List<LecturerRoom> rooms = records.into(LecturerRoom.class).stream()
            .filter(room -> room.getId() != null).collect(Collectors.toList());
        return new LectureRoomDto(lecturer, rooms);
    }

    public List<LecturerSchedule> selectLecturerSchedule() {
        return jooq.select()
                .from(Tables.LECTURER_SCHEDULE)
                .fetch()
                .into(Tables.LECTURER_SCHEDULE)
                .into(LecturerSchedule.class);
    }
}
