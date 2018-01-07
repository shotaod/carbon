package org.carbon.sample.v2.web.task;

import java.util.List;

import org.carbon.component.annotation.Inject;
import org.carbon.sample.v2.conf.auth.identity.HerokuAuthIdentity;
import org.carbon.web.annotation.Action;
import org.carbon.web.annotation.Controller;
import org.carbon.web.annotation.PathVariable;
import org.carbon.web.annotation.RequestBody;
import org.carbon.web.annotation.Session;
import org.carbon.web.core.response.HtmlResponse;
import org.carbon.web.core.response.HttpOperation;
import org.carbon.web.core.response.RedirectOperation;
import org.carbon.web.core.validation.HandyValidationResult;
import org.carbon.web.def.HttpMethod;

/**
 * @author Shota Oda 2017/08/14.
 */
@Controller("/task")
public class TaskController {
    @Inject
    private TaskService taskService;

    @Action(url = "about", method = HttpMethod.GET)
    public HtmlResponse getAbout() {
        return new HtmlResponse("/task/about");
    }

    @Action(url = "", method = HttpMethod.GET)
    public HtmlResponse getTasks(@Session HerokuAuthIdentity user) {
        List<TaskDto> tasks = taskService.findTasks(user.getId());

        HtmlResponse htmlResponse = new HtmlResponse("/task/index");
        htmlResponse.putData("data", tasks);
        return htmlResponse;
    }

    @Action(url = "", method = HttpMethod.POST)
    public HttpOperation postTask(@Session HerokuAuthIdentity user, @RequestBody TaskPostForm form, HandyValidationResult vr) {
        taskService.addTask(user.getId(), form);
        return RedirectOperation.to("/task");
    }

    @Action(url = "/{id}/drop", method = HttpMethod.POST)
    public HttpOperation postDropTask(@Session HerokuAuthIdentity user, @PathVariable("id") Long todoId) {
        taskService.dropTask(user.getId(), todoId);
        return RedirectOperation.to("/task");
    }
}
