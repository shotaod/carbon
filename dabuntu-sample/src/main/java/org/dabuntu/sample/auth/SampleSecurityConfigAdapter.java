package org.dabuntu.sample.auth;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Inject;
import org.dabuntu.sample.auth.basic.BasicAuthFinisher;
import org.dabuntu.sample.auth.basic.BasicAuthRequestMapper;
import org.dabuntu.sample.auth.basic.BasicAuthResponseCreator;
import org.dabuntu.sample.auth.form.FormAuthFinisher;
import org.dabuntu.sample.auth.form.FormAuthRequestMapper;
import org.dabuntu.sample.auth.form.FormAuthResponseCreator;
import org.dabuntu.sample.auth.identity.SampleAuthIdentifier;
import org.dabuntu.sample.auth.identity.SampleAuthIdentity;
import org.dabuntu.web.auth.SecurityConfigAdapter;
import org.dabuntu.web.auth.SecurityConfiguration;
import org.dabuntu.web.def.HttpMethod;

/**
 * @author ubuntu 2016/11/03.
 */
@Component
public class SampleSecurityConfigAdapter implements SecurityConfigAdapter {

	@Inject
	private BasicAuthRequestMapper basicMapper;
	@Inject
	private SampleAuthIdentifier sampleIdentifier;
	@Inject
	private BasicAuthFinisher basicFinisher;
	@Inject
	private BasicAuthResponseCreator basicResponse;
	@Inject
	private FormAuthRequestMapper formMapper;
	@Inject
	private FormAuthResponseCreator formResponse;
	@Inject
	private FormAuthFinisher formFinisher;

	@Override
	public void configure(SecurityConfiguration config) {
		config
			.define(SampleAuthIdentity.class)
				.base("/basic/")
				.endPoint(HttpMethod.GET, "/basic/auth")
				.redirect("/basic")
				.requestMapper(basicMapper)
				.response(basicResponse)
				.identifier(sampleIdentifier)
				.finisher(basicFinisher)
			.end()
//			.define()
//				.base("/digest")
//			.end()
			.define(SampleAuthIdentity.class)
				.base("/form/")
				.endPoint(HttpMethod.POST, "/form/auth")
				.redirect("/form")
				.requestMapper(formMapper)
				.response(formResponse)
				.identifier(sampleIdentifier)
				.finisher(formFinisher)
			.end()
			;
	}
}
