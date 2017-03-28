package org.carbon.sample.heroku.web.sample.rest.todo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.carbon.sample.heroku.ext.jooq.tables.records.TodoRecord;

/**
 * @author ubuntu 2017/03/28.
 */
@Getter
@AllArgsConstructor
public class TodoItem {
    private long id;
    private String text;

    public TodoItem(TodoRecord todo) {
        this.id = todo.getId();
        this.text = todo.getText();
    }
}
