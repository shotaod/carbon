package org.carbon.sample.web.consumer;

import lombok.NonNull;
import org.carbon.persistent.annotation.Transactional;
import org.carbon.sample.ext.jooq.Tables;
import org.carbon.sample.ext.jooq.tables.daos.LecturerApplyHistoryDao;
import org.carbon.sample.ext.jooq.tables.daos.LecturerDao;
import org.carbon.sample.ext.jooq.tables.daos.LecturerScheduleDao;
import org.carbon.sample.ext.jooq.tables.pojos.*;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.sample.web.consumer.dto.LectureRoomDto;
import org.carbon.sample.web.consumer.dto.LecturerIndex;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Shota Oda 2016/11/27.
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
                .from(Tables.LECTURER.leftJoin(Tables.ASSET).onKey())
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
