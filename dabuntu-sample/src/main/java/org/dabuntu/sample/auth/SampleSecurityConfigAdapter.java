package org.dabuntu.sample.auth;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Inject;
import org.dabuntu.sample.auth.basic.BasicAuthFinisher;
import org.dabuntu.sample.auth.basic.BasicAuthRequestMapper;
import org.dabuntu.sample.auth.basic.BasicAuthResponseCreator;
import org.dabuntu.sample.auth.form.FormAuthFinisher;
import org.dabuntu.sample.auth.form.FormAuthRequestMapper;
import org.dabuntu.sample.auth.form.FormAuthResponseCreator;
import org.dabuntu.sample.auth.basic.identity.SampleBasicAuthIdentifier;
import org.dabuntu.sample.auth.basic.identity.SampleBasicAuthIdentity;
import org.dabuntu.sample.auth.form.identity.SampleFormAuthIdentifier;
import org.dabuntu.sample.auth.form.identity.SampleFormAuthIdentity;
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
	private BasicAuthFinisher basicFinisher;
	@Inject
	private BasicAuthResponseCreator basicResponse;

	// -----------------------------------------------------
	//                                               for Form Auth
	//                                               -------
	@Inject
	private FormAuthRequestMapper formMapper;
	@Inject
	private SampleFormAuthIdentifier formIdentifier;
	@Inject
	private FormAuthResponseCreator formResponse;
	@Inject
	private FormAuthFinisher formFinisher;

	@Override
	public void configure(SecurityConfiguration config) {
		config
			.define(SampleBasicAuthIdentity.class)
				.base("/basic/")
				.endPoint(HttpMethod.GET, "/basic/auth")
				.redirect("/basic")
				.requestMapper(basicMapper)
				.response(basicResponse)
				.identifier(basicIdentifier)
				.finisher(basicFinisher)
			.end()
//			.define()
//				.base("/digest")
//			.end()
			.define(SampleFormAuthIdentity.class)
				.base("/form/")
				.endPoint(HttpMethod.POST, "/form/auth")
				.redirect("/form")
				.requestMapper(formMapper)
				.response(formResponse)
				.identifier(formIdentifier)
				.finisher(formFinisher)
			.end()
			;
	}
}
