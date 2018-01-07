package org.carbon.sample.v2.web.task;

import java.util.List;
import java.util.stream.Collectors;

import lombok.NonNull;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.persistent.annotation.Transactional;
import org.carbon.sample.v2.ext.jooq.tables.daos.TaskDao;
import org.carbon.sample.v2.web.api.IdResponse;
import org.jooq.DSLContext;

import static org.carbon.sample.v2.ext.jooq.Tables.TASK;

/**
 * @author ubuntu 2017/03/28.
 */
@Component
public class TaskService {

    @Inject
    private DSLContext jooq;
    @Inject
    private TaskDao taskDao;

    public List<TaskDto> findTasks(@NonNull Long userId) {
        return jooq.selectFrom(TASK)
                .where(
                    TASK.USER_ID.eq(userId),
                    TASK.AVAILABLE.eq(true))
                .fetch()
                .stream()
                .map(TaskDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public IdResponse addTask(@NonNull Long userId, @NonNull TaskPostForm form) {
        Long id = jooq.insertInto(TASK)
                .columns(TASK.USER_ID, TASK.TITLE, TASK.DESCRIPTION)
                .values(userId, form.getTitle(), form.getDescription())
                .returning(TASK.ID)
                .fetchOne()
                .getId();
        return new IdResponse(id);
    }

    @Transactional
    public void dropTask(@NonNull Long userId, @NonNull Long taskId) {
        Long validTaskId = jooq.selectFrom(TASK)
                .where(TASK.ID.eq(taskId))
                .and(TASK.USER_ID.eq(userId))
                .fetchOne()
                .getId();
        if (validTaskId == null) {
            // do nothing
            return;
        }
        jooq.update(TASK)
            .set(TASK.AVAILABLE, false)
            .where(TASK.ID.eq(validTaskId))
            .execute();
    }
}
