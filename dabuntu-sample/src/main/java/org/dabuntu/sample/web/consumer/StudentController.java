package org.dabuntu.sample.web.consumer;

import org.dabunt.sample.tables.pojos.LecturerSchedule;
import org.dabuntu.component.annotation.Inject;
import org.dabuntu.sample.auth.consumer.ConsumerAuthIdentity;
import org.dabuntu.web.annotation.Action;
import org.dabuntu.web.annotation.Controller;
import org.dabuntu.web.annotation.Session;
import org.dabuntu.web.core.response.HtmlResponse;
import org.dabuntu.web.def.HttpMethod;

import java.util.List;

/**
 * @author ubuntu 2016/11/23.
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
	public HtmlResponse authSuccessPost() {
		return new HtmlResponse("");
	}

	@Action(url = "/consumer/home", method = HttpMethod.GET)
	public HtmlResponse homeGet() {
		HtmlResponse response = new HtmlResponse("/consumer/home");
		List<LecturerSchedule> schedules = appService.selectLecturerSchedule();
		response.putData("models", schedules);
		return response;
	}

	@Action(url = "/consumer/apply", method = HttpMethod.POST)
	public void applyPost(@Session ConsumerAuthIdentity identity) {
		Long id = identity.getStudent().getId();
		appService.apply(null, null, id);
	}
}
