package org.dabuntu.sample.auth;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Inject;
import org.dabuntu.sample.auth.basic.BasicAuthFinisher;
import org.dabuntu.sample.auth.basic.BasicAuthIdentifier;
import org.dabuntu.sample.auth.basic.BasicAuthIdentity;
import org.dabuntu.sample.auth.basic.BasicAuthRequestMapper;
import org.dabuntu.sample.auth.basic.BasicAuthResponseCreator;
import org.dabuntu.web.auth.SecurityConfiguration;
import org.dabuntu.web.auth.SecurityConfigAdapter;
import org.dabuntu.web.def.HttpMethod;

/**
 * @author ubuntu 2016/11/03.
 */
@Component
public class SampleSecurityConfigAdapter implements SecurityConfigAdapter {

	@Inject
	private BasicAuthRequestMapper basicMapper;
	@Inject
	private BasicAuthIdentifier basicIdentifier;
	@Inject
	private BasicAuthFinisher basicFinisher;
	@Inject
	private BasicAuthResponseCreator basicResponse;
	@Override
	public void configure(SecurityConfiguration config) {
		config
			.define(BasicAuthIdentity.class)
				.base("/basic/")
				.endPoint(HttpMethod.GET, "/basic/auth")
				.redirect("/basic")
				.requestMapper(basicMapper)
				.responese(basicResponse)
				.identifier(basicIdentifier)
				.finisher(basicFinisher)
			.end()
//			.define()
//				.base("/digest")
//			.end()
//			.define()
//				.base("/login/")
//				.redirect("/login")
//				.endPoint("/login/auth")
//			.end()
			;
	}
}
