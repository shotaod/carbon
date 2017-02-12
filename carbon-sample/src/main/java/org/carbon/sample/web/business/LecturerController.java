package org.carbon.sample.web.business;

import org.carbon.sample.auth.business.BusinessAuthIdentity;
import org.carbon.sample.web.business.dto.ScheduleDto;
import org.carbon.sample.tables.pojos.Lecturer;
import org.carbon.component.annotation.Inject;
import org.carbon.sample.web.business.dto.LecturerRoomDto;
import org.carbon.web.annotation.Action;
import org.carbon.web.annotation.Controller;
import org.carbon.web.annotation.RequestBody;
import org.carbon.web.annotation.Session;
import org.carbon.web.annotation.Validate;
import org.carbon.web.core.response.HtmlResponse;
import org.carbon.web.core.response.HttpOperation;
import org.carbon.web.core.response.RedirectOperation;
import org.carbon.web.core.validation.SimpleValidationResult;
import org.carbon.web.def.HttpMethod;

import java.util.List;

/**
 * @author Shota Oda 2016/11/23.
 */
@Controller
public class LecturerController {

    @Inject
    private LecturerAppService appService;

    @Action(url = "/business", method = HttpMethod.GET)
    public HtmlResponse indexGet() {
        return new HtmlResponse("/business/login");
    }

    // -----------------------------------------------------
    //                                               authentication
    //                                               -------
    @Action(url = "/business/login", method = HttpMethod.GET)
    public HtmlResponse loginGet() {
        return new HtmlResponse("/business/login");
    }

    @Action(url = "/business/auth", method = HttpMethod.POST)
    public HttpOperation authSuccessPost() {
        return RedirectOperation.to("/business/home");
    }

    // -----------------------------------------------------
    //                                               pages
    //                                               -------
    @Action(url = "/business/home", method = HttpMethod.GET)
    public HtmlResponse homeGet(@Session BusinessAuthIdentity authIdentity) {
        HtmlResponse response = new HtmlResponse("/business/lecturer_room");

        LecturerRoomDto model = appService.selectRooms(authIdentity.getUser().getId());
        response.putData("model", model);
        return response;
    }

    // -----------------------------------------------------
    //                                               Room
    //                                               -------
    @Action(url = "/business/room", method = HttpMethod.GET)
    public HtmlResponse roomGet(@Session BusinessAuthIdentity authIdentity) {
        HtmlResponse response = new HtmlResponse("/business/lecturer_room");

        LecturerRoomDto model = appService.selectRooms(authIdentity.getUser().getId());
        response.putData("model", model);
        return response;
    }

    @Action(url = "/business/room/create", method = HttpMethod.GET)
    public HtmlResponse roomCreateGet(@Session BusinessAuthIdentity authIdentity) {
        return new HtmlResponse("business/lecturer_room_create");
    }

    @Action(url = "/business/room/create", method = HttpMethod.POST)
    public HtmlResponse roomCreatePost(@Session BusinessAuthIdentity authIdentity,
                                       @RequestBody @Validate RoomCreateForm form,
                                       SimpleValidationResult vr) {
        if (vr.existError()) {
            HtmlResponse response = new HtmlResponse("business/lecturer_room_create");
            response.putData("errors", vr.getViolationResults());
            return response;
        }
        Long lecturerId = authIdentity.getUser().getId();
        appService.insertRoom(form, lecturerId);
        LecturerRoomDto model = appService.selectRooms(lecturerId);
        HtmlResponse response = new HtmlResponse("business/lecturer_room");
        response.putData("model", model);
        return response;
    }

    // -----------------------------------------------------
    //                                               profile
    //                                               -------
    @Action(url="/business/profile", method = HttpMethod.GET)
    public HtmlResponse profileGet(@Session BusinessAuthIdentity authIdentity) {
        Lecturer lecturer = appService.selectLecturer(authIdentity.getUser().getId());

        HtmlResponse response = new HtmlResponse("/business/lecturer_profile");
        response.putData("model", lecturer);
        return response;
    }

    @Action(url="/business/profile/edit", method = HttpMethod.GET)
    public HtmlResponse profileEditGet(@Session BusinessAuthIdentity authIdentity) {
        Lecturer model = appService.selectLecturer(authIdentity.getUser().getId());
        HtmlResponse response = new HtmlResponse("/business/lecturer_profile_edit");
        response.putData("model", model);
        return response;
    }

    @Action(url="/business/profile/edit", method = HttpMethod.POST)
    public HtmlResponse profileEditPost(@Session BusinessAuthIdentity authIdentity,
                                        @RequestBody @Validate LecturerProfileForm form,
                                        SimpleValidationResult vr) {
        if (vr.existError()) {
            HtmlResponse response = new HtmlResponse("/buisness/lecturer_profile_edit");
            response.putData("model", vr.getViolationResults());
            return response;
        }
        Lecturer model = appService.updateProfile(form, authIdentity.getUser().getId());

        HtmlResponse response = new HtmlResponse("/business/lecturer_profile");
        response.putData("model", model);
        return response;
    }

    // -----------------------------------------------------
    //                                               schedule
    //                                               -------
    @Action(url = "/business/schedule", method = HttpMethod.GET)
    public HtmlResponse scheduleGet(@Session BusinessAuthIdentity authIdentity) {
        HtmlResponse response = new HtmlResponse("/business/schedule");

        List<ScheduleDto> schedules = appService.selectSchedules(authIdentity.getUser().getId());
        response.putData("models", schedules);

        return response;
    }

    @Action(url = "/business/schedule", method = HttpMethod.POST)
    public HtmlResponse schedulePost(@Session BusinessAuthIdentity authIdentity,
                                     @RequestBody ScheduleForm form) {
        appService.insertSchedule(form, authIdentity.getUser().getId());

        HtmlResponse response = new HtmlResponse("/business/schedule");

        List<ScheduleDto> schedules = appService.selectSchedules(authIdentity.getUser().getId());
        response.putData("models", schedules);

        return response;
    }

    @Action(url = "/business/apply", method = HttpMethod.GET)
    public HtmlResponse applyGet(@Session BusinessAuthIdentity authIdentity) {
        return new HtmlResponse("business/timeline");
    }
}
