package org.dabuntu.sample.auth;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Inject;
import org.dabuntu.sample.auth.basic.BasicAuthEvent;
import org.dabuntu.sample.auth.basic.BasicAuthRequestMapper;
import org.dabuntu.sample.auth.basic.identity.SampleBasicAuthIdentifier;
import org.dabuntu.sample.auth.form.FormAuthEvent;
import org.dabuntu.sample.auth.form.FormAuthRequestMapper;
import org.dabuntu.sample.auth.form.identity.SampleFormAuthIdentifier;
import org.dabuntu.web.auth.SecurityConfigAdapter;
import org.dabuntu.web.auth.SecurityConfiguration;
import org.dabuntu.web.def.HttpMethod;

/**
 * @author ubuntu 2016/11/03.
 */
@Component
public class SampleSecurityConfigAdapter implements SecurityConfigAdapter {

	// -----------------------------------------------------
	//                                               for Basic Auth
	//                                               -------
	@Inject
	private BasicAuthRequestMapper basicMapper;
	@Inject
	private SampleBasicAuthIdentifier basicIdentifier;
	@Inject
	private BasicAuthEvent basicFinisher;

	// -----------------------------------------------------
	//                                               for Form Auth
	//                                               -------
	@Inject
	private FormAuthRequestMapper formMapper;
	@Inject
	private SampleFormAuthIdentifier formIdentifier;
	@Inject
	private FormAuthEvent formFinisher;

	@Override
	public void configure(SecurityConfiguration config) {
		config
			.define()
				.identifier(basicIdentifier)
				.base("/basic/")
				.endPoint(HttpMethod.GET, "/basic/**")
				.logout("/basic/logout")
				.redirect("/basic")
				.requestMapper(basicMapper)
				.finisher(basicFinisher)
			.end()
//			.define()
//				.base("/digest")
//			.end()
			.define()
				.identifier(formIdentifier)
				.base("/form/")
				.endPoint(HttpMethod.POST, "/form/auth")
				.logout("/form/logout")
				.redirect("/form")
				.requestMapper(formMapper)
				.finisher(formFinisher)
			.end()
			;
	}
}
