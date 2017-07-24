package org.carbon.sample.heroku.web.sample.rest;

import org.carbon.component.annotation.Inject;
import org.carbon.sample.heroku.web.sample.rest.common.request.TodoPostForm;
import org.carbon.sample.heroku.web.sample.rest.common.response.IdResponse;
import org.carbon.sample.heroku.web.sample.rest.common.response.ListResponse;
import org.carbon.sample.heroku.web.sample.rest.todo.TodoItem;
import org.carbon.sample.heroku.web.sample.rest.todo.TodoParams;
import org.carbon.web.annotation.Action;
import org.carbon.web.annotation.Controller;
import org.carbon.web.annotation.PathVariable;
import org.carbon.web.annotation.RequestBody;
import org.carbon.web.annotation.RequestParam;
import org.carbon.web.core.validation.SimpleValidationResult;
import org.carbon.web.def.HttpMethod;

/**
 * @author ubuntu 2017/03/28.
 */
@Controller("/api")
public class RestApiController {

    @Inject
    private TodoService todoService;

    @Action(url = "/todos", method = HttpMethod.GET)
    public ListResponse<TodoItem> getTodos() {
        return todoService.findTodos(1L);
    }

    @Action(url = "/todos", method = HttpMethod.POST)
    public IdResponse postTodo(@RequestBody TodoPostForm form, SimpleValidationResult vr) {
        return todoService.addTodo(1L, form);
    }

    @Action(url = "/todos/{id}", method = HttpMethod.DELETE)
    public void deleteTodo(@PathVariable("id") Long todoId) {
        todoService.dropTodo(todoId);
    }
}
