package org.carbon.sample.web.consumer;

import java.util.List;

import org.carbon.component.annotation.Assemble;
import org.carbon.sample.auth.consumer.ConsumerAuthIdentity;
import org.carbon.sample.web.consumer.dto.LectureRoomDto;
import org.carbon.sample.web.consumer.dto.LecturerIndex;
import org.carbon.web.annotation.Action;
import org.carbon.web.annotation.Controller;
import org.carbon.web.annotation.PathVariable;
import org.carbon.web.annotation.scope.SessionScope;
import org.carbon.web.translate.dto.Html;
import org.carbon.web.def.HttpMethod;
import org.carbon.web.translate.dto.Redirect;
import org.carbon.web.translate.dto.Transfer;

/**
 * @author Shota Oda 2016/11/23.
 */
@Controller
public class StudentController {
    @Assemble
    private StudentAppService appService;

    @Action(path = "/consumer", method = HttpMethod.GET)
    public Html indexGet() {
        return new Html("/consumer/login");
    }

    @Action(path = "/consumer/login", method = HttpMethod.GET)
    public Html loginGet() {
        return new Html("/consumer/login");
    }

    @Action(path = "/consumer/auth", method = HttpMethod.POST)
    public Transfer authSuccessPost() {
        return new Redirect("/consumer/lecturers");
    }

    @Action(path = "/consumer/lecturers", method = HttpMethod.GET)
    public Html homeGet() {
        Html response = new Html("/consumer/lecturer_list");
        List<LecturerIndex> lecturers = appService.selectLecturers();
        response.putData("models", lecturers);
        return response;
    }

    @Action(path = "/consumer/lecturer/{lecturerId}/room", method = HttpMethod.GET)
    public Html lecturerRoomGet(@PathVariable("lecturerId") String lecturerId) {
        Html response = new Html("/consumer/lecturer_room");
        LectureRoomDto model = appService.selectLecturerRoom(Long.parseLong(lecturerId));
        response.putData("model", model);
        return response;
    }

    @Action(path = "/consumer/apply", method = HttpMethod.POST)
    public void applyPost(@SessionScope ConsumerAuthIdentity identity) {
        Long id = identity.getStudent().getId();
        appService.apply(null, null, id);
    }
}
