package org.carbon.sample.web.business;

import java.util.List;

import org.carbon.component.annotation.Assemble;
import org.carbon.sample.auth.business.BusinessAuthIdentity;
import org.carbon.sample.ext.jooq.tables.pojos.Lecturer;
import org.carbon.sample.web.business.dto.LecturerRoomDto;
import org.carbon.sample.web.business.dto.ScheduleDto;
import org.carbon.web.annotation.Action;
import org.carbon.web.annotation.Controller;
import org.carbon.web.annotation.RequestBody;
import org.carbon.web.annotation.scope.SessionScope;
import org.carbon.web.annotation.Validate;
import org.carbon.web.core.validation.HandyValidationResult;
import org.carbon.web.def.HttpMethod;
import org.carbon.web.translate.dto.Html;
import org.carbon.web.translate.dto.Redirect;
import org.carbon.web.translate.dto.Transfer;

/**
 * @author Shota Oda 2016/11/23.
 */
@Controller
public class LecturerController {

    @Assemble
    private LecturerAppService appService;

    @Action(path = "/business", method = HttpMethod.GET)
    public Html indexGet() {
        return new Html("/business/login");
    }

    // -----------------------------------------------------
    //                                               authentication
    //                                               -------
    @Action(path = "/business/login", method = HttpMethod.GET)
    public Html loginGet() {
        return new Html("/business/login");
    }

    @Action(path = "/business/auth", method = HttpMethod.POST)
    public Transfer authSuccessPost() {
        return new Redirect("/business/home");
    }

    // -----------------------------------------------------
    //                                               pages
    //                                               -------
    @Action(path = "/business/home", method = HttpMethod.GET)
    public Html homeGet(@SessionScope BusinessAuthIdentity authIdentity) {
        Html response = new Html("/business/lecturer_room");

        LecturerRoomDto model = appService.selectRooms(authIdentity.getUser().getId());
        response.putData("model", model);
        return response;
    }

    // -----------------------------------------------------
    //                                               Room
    //                                               -------
    @Action(path = "/business/room", method = HttpMethod.GET)
    public Html roomGet(@SessionScope BusinessAuthIdentity authIdentity) {
        Html response = new Html("/business/lecturer_room");

        LecturerRoomDto model = appService.selectRooms(authIdentity.getUser().getId());
        response.putData("model", model);
        return response;
    }

    @Action(path = "/business/room/create", method = HttpMethod.GET)
    public Html roomCreateGet(@SessionScope BusinessAuthIdentity authIdentity) {
        return new Html("business/lecturer_room_create");
    }

    @Action(path = "/business/room/create", method = HttpMethod.POST)
    public Html roomCreatePost(@SessionScope BusinessAuthIdentity authIdentity,
                               @RequestBody @Validate RoomCreateForm form,
                               HandyValidationResult vr) {
        if (vr.existError()) {
            Html response = new Html("business/lecturer_room_create");
            response.putData("errors", vr.getViolationResults());
            return response;
        }
        Long lecturerId = authIdentity.getUser().getId();
        appService.insertRoom(form, lecturerId);
        LecturerRoomDto model = appService.selectRooms(lecturerId);
        Html response = new Html("business/lecturer_room");
        response.putData("model", model);
        return response;
    }

    // -----------------------------------------------------
    //                                               profile
    //                                               -------
    @Action(path = "/business/profile", method = HttpMethod.GET)
    public Html profileGet(@SessionScope BusinessAuthIdentity authIdentity) {
        Lecturer lecturer = appService.selectLecturer(authIdentity.getUser().getId());

        Html response = new Html("/business/lecturer_profile");
        response.putData("model", lecturer);
        return response;
    }

    @Action(path = "/business/profile/edit", method = HttpMethod.GET)
    public Html profileEditGet(@SessionScope BusinessAuthIdentity authIdentity) {
        Lecturer model = appService.selectLecturer(authIdentity.getUser().getId());
        Html response = new Html("/business/lecturer_profile_edit");
        response.putData("model", model);
        return response;
    }

    @Action(path = "/business/profile/edit", method = HttpMethod.POST)
    public Html profileEditPost(@SessionScope BusinessAuthIdentity authIdentity,
                                @RequestBody @Validate LecturerProfileForm form,
                                HandyValidationResult vr) {
        if (vr.existError()) {
            Html response = new Html("/buisness/lecturer_profile_edit");
            response.putData("model", vr.getViolationResults());
            return response;
        }
        Lecturer model = appService.updateProfile(form, authIdentity.getUser().getId());

        Html response = new Html("/business/lecturer_profile");
        response.putData("model", model);
        return response;
    }

    // -----------------------------------------------------
    //                                               schedule
    //                                               -------
    @Action(path = "/business/schedule", method = HttpMethod.GET)
    public Html scheduleGet(@SessionScope BusinessAuthIdentity authIdentity) {
        Html response = new Html("/business/schedule");

        List<ScheduleDto> schedules = appService.selectSchedules(authIdentity.getUser().getId());
        response.putData("models", schedules);

        return response;
    }

    @Action(path = "/business/schedule", method = HttpMethod.POST)
    public Html schedulePost(@SessionScope BusinessAuthIdentity authIdentity,
                             @RequestBody ScheduleForm form) {
        appService.insertSchedule(form, authIdentity.getUser().getId());

        Html response = new Html("/business/schedule");

        List<ScheduleDto> schedules = appService.selectSchedules(authIdentity.getUser().getId());
        response.putData("models", schedules);

        return response;
    }

    @Action(path = "/business/apply", method = HttpMethod.GET)
    public Html applyGet(@SessionScope BusinessAuthIdentity authIdentity) {
        return new Html("business/timeline");
    }
}
