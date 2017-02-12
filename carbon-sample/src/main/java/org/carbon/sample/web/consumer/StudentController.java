package org.carbon.sample.web.consumer;

import org.carbon.component.annotation.Inject;
import org.carbon.sample.auth.consumer.ConsumerAuthIdentity;
import org.carbon.sample.web.consumer.dto.LectureRoomDto;
import org.carbon.sample.web.consumer.dto.LecturerIndex;
import org.carbon.web.annotation.Action;
import org.carbon.web.annotation.Controller;
import org.carbon.web.annotation.PathVariable;
import org.carbon.web.annotation.Session;
import org.carbon.web.core.response.HtmlResponse;
import org.carbon.web.core.response.HttpOperation;
import org.carbon.web.core.response.RedirectOperation;
import org.carbon.web.def.HttpMethod;

import java.util.List;

/**
 * @author Shota Oda 2016/11/23.
 */
@Controller
public class StudentController {
    @Inject
    private StudentAppService appService;

    @Action(url = "/consumer", method = HttpMethod.GET)
    public HtmlResponse indexGet() {
        return new HtmlResponse("/consumer/login");
    }

    @Action(url = "/consumer/login", method = HttpMethod.GET)
    public HtmlResponse loginGet() {
        return new HtmlResponse("/consumer/login");
    }

    @Action(url = "/consumer/auth", method = HttpMethod.POST)
    public HttpOperation authSuccessPost() {
        return RedirectOperation.to("/consumer/lecturers");
    }

    @Action(url = "/consumer/lecturers", method = HttpMethod.GET)
    public HtmlResponse homeGet() {
        HtmlResponse response = new HtmlResponse("/consumer/lecturer_list");
        List<LecturerIndex> lecturers = appService.selectLecturers();
        response.putData("models", lecturers);
        return response;
    }

    @Action(url = "/consumer/lecturer/{lecturerId}/room", method = HttpMethod.GET)
    public HtmlResponse lecturerRoomGet(@PathVariable("lecturerId") String lecturerId) {
        HtmlResponse response = new HtmlResponse("/consumer/lecturer_room");
        LectureRoomDto model = appService.selectLecturerRoom(Long.parseLong(lecturerId));
        response.putData("model", model);
        return response;
    }

    @Action(url = "/consumer/apply", method = HttpMethod.POST)
    public void applyPost(@Session ConsumerAuthIdentity identity) {
        Long id = identity.getStudent().getId();
        appService.apply(null, null, id);
    }
}
