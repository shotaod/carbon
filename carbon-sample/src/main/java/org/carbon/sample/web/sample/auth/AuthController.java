package org.carbon.sample.web.sample.auth;

import org.carbon.sample.auth.basic.identity.SampleBasicAuthIdentity;
import org.carbon.sample.web.sample.index.UserInfoModel;
import org.carbon.sample.auth.form.identity.FormAuthIdentity;
import org.carbon.sample.web.session.SessionInfo;
import org.carbon.web.annotation.Action;
import org.carbon.web.annotation.Controller;
import org.carbon.web.annotation.PathVariable;
import org.carbon.web.annotation.Session;
import org.carbon.web.core.response.HtmlResponse;
import org.carbon.web.def.HttpMethod;

/**
 * @author ubuntu 2016/11/05.
 */
@Controller
public class AuthController {
	// -----------------------------------------------------
	//                                               Basic Auth
	//                                               -------
	@Action(url = "/basic", method = HttpMethod.GET)
	public HtmlResponse getBasicIndex() {
		return new HtmlResponse("/basic/index");
	}
	@Action(url = "/basic/secret/{number}", method = HttpMethod.GET)
	public HtmlResponse requestBasicSecret(@PathVariable(value = "number") String number, @Session SampleBasicAuthIdentity userSession) {
		HtmlResponse response = new HtmlResponse("basic/secret");

		UserInfoModel model = new UserInfoModel();
		model.setUsername(userSession.username());
		model.setPassword(userSession.cryptPassword());

		response.putData("model", model);
		response.putData("secretNumber", number);
		return response;
	}

	@Action(url = "/form", method = HttpMethod.GET)
	public HtmlResponse getLogin() {
		return new HtmlResponse("form/login");
	}

	@Action(url = "/form/auth", method = HttpMethod.POST)
	public HtmlResponse postFormAuth() {
		return new HtmlResponse("/form/index");
	}

	@Action(url = "/form/secret", method = HttpMethod.GET)
	public HtmlResponse getFormSecret(
			@Session FormAuthIdentity userSession,
			@Session SessionInfo sessionInfo) {
		HtmlResponse response = new HtmlResponse("/form/secret");
		UserInfoModel model = new UserInfoModel();
		model.setUsername(userSession.username());
		model.setPassword(userSession.cryptPassword());
		response.putData("model", model);
		response.putData("loginInfo", sessionInfo);
		return response;
	}
}
