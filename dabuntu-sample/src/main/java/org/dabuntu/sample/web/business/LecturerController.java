package org.dabuntu.sample.web.business;

import org.dabuntu.component.annotation.Inject;
import org.dabuntu.sample.auth.business.BusinessAuthIdentity;
import org.dabuntu.web.annotation.Action;
import org.dabuntu.web.annotation.Controller;
import org.dabuntu.web.annotation.RequestBody;
import org.dabuntu.web.annotation.Session;
import org.dabuntu.web.core.response.HtmlResponse;
import org.dabuntu.web.core.response.HttpOperation;
import org.dabuntu.web.core.response.RedirectOperation;
import org.dabuntu.web.def.HttpMethod;

import java.util.List;

/**
 * @author ubuntu 2016/11/23.
 */
@Controller
public class LecturerController {

	@Inject
	private LecturerAppService appService;

	@Action(url = "/business", method = HttpMethod.GET)
	public HtmlResponse indexGet() {
		return new HtmlResponse("/business/login");
	}

	@Action(url = "/business/login", method = HttpMethod.GET)
	public HtmlResponse loginGet() {
		return new HtmlResponse("/business/login");
	}

	@Action(url = "/business/auth", method = HttpMethod.POST)
	public HttpOperation authSuccessPost() {
		return new RedirectOperation("/business/schedule");
	}

	@Action(url = "/business/register", method = HttpMethod.GET)
	public HtmlResponse registerGet(@Session BusinessAuthIdentity authIdentity) {
		return new HtmlResponse("business/timeline");
	}

	@Action(url = "/business/schedule", method = HttpMethod.GET)
	public HtmlResponse scheduleGet(@Session BusinessAuthIdentity authIdentity) {
		HtmlResponse response = new HtmlResponse("/business/schedule");

		List<ScheduleEntity> schedules = appService.selectSchedules(authIdentity.getUser().getId());
		response.putData("models", schedules);

		return response;
	}

	@Action(url = "/business/schedule", method = HttpMethod.POST)
	public HtmlResponse schedulePost(@Session BusinessAuthIdentity authIdentity,
									 @RequestBody ScheduleForm form) {
		appService.insertSchedule(form, authIdentity.getUser().getId());

		HtmlResponse response = new HtmlResponse("/business/schedule");

		List<ScheduleEntity> schedules = appService.selectSchedules(authIdentity.getUser().getId());
		response.putData("models", schedules);

		return response;
	}

	@Action(url = "/business/apply", method = HttpMethod.GET)
	public HtmlResponse applyGet(@Session BusinessAuthIdentity authIdentity) {
		return new HtmlResponse("business/timeline");
	}
}
