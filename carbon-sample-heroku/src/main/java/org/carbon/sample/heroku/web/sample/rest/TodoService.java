package org.carbon.sample.heroku.web.sample.rest;

import java.util.List;
import java.util.stream.Collectors;

import lombok.NonNull;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.persistent.annotation.Transactional;
import org.carbon.sample.heroku.ext.jooq.tables.daos.TodoDao;
import org.carbon.sample.heroku.web.sample.rest.common.request.TodoPostForm;
import org.carbon.sample.heroku.web.sample.rest.common.response.IdResponse;
import org.carbon.sample.heroku.web.sample.rest.common.response.ListResponse;
import org.carbon.sample.heroku.web.sample.rest.todo.TodoItem;
import org.jooq.DSLContext;

import static org.carbon.sample.heroku.ext.jooq.Tables.TODO;

/**
 * @author ubuntu 2017/03/28.
 */
@Component
public class TodoService {

    @Inject
    private DSLContext jooq;
    @Inject
    private TodoDao todoDao;

    public ListResponse<TodoItem> findTodos(@NonNull Long userId) {
        List<TodoItem> items = jooq.selectFrom(TODO)
                .where(
                        TODO.USER_ID.eq(userId),
                        TODO.AVAILABLE.eq(true))
                .fetch()
                .stream()
                .map(TodoItem::new)
                .collect(Collectors.toList());
        return new ListResponse<>(items);
    }

    @Transactional
    public IdResponse addTodo(@NonNull Long userId, @NonNull TodoPostForm form) {
        Long id = jooq.insertInto(TODO)
                .columns(TODO.USER_ID, TODO.TEXT, TODO.AVAILABLE)
                .values(userId, form.getText(), true)
                .returning(TODO.ID)
                .fetchOne()
                .getId();
        return new IdResponse(id);
    }

    @Transactional
    public void dropTodo(@NonNull Long todoId) {
        jooq.update(TODO)
            .set(TODO.AVAILABLE, true)
            .where(TODO.ID.eq(todoId))
            .execute();
    }
}
