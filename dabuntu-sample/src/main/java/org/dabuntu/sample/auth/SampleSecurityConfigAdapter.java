package org.dabuntu.sample.auth;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Inject;
import org.dabuntu.sample.auth.basic.BasicAuthEvent;
import org.dabuntu.sample.auth.basic.BasicAuthRequestMapper;
import org.dabuntu.sample.auth.basic.identity.SampleBasicAuthIdentifier;
import org.dabuntu.sample.auth.business.BusinessAuthEvent;
import org.dabuntu.sample.auth.business.BusinessAuthIdentifier;
import org.dabuntu.sample.auth.consumer.ConsumerAuthEvent;
import org.dabuntu.sample.auth.consumer.ConsumerAuthIdentifier;
import org.dabuntu.sample.auth.form.FormAuthEvent;
import org.dabuntu.sample.auth.form.FormAuthRequestMapper;
import org.dabuntu.sample.auth.form.identity.FormAuthIdentifier;
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
	private FormAuthIdentifier formIdentifier;
	@Inject
	private FormAuthEvent formFinisher;

	// -----------------------------------------------------
	//                                               for Consumer Auth
	//                                               -------
	@Inject
	private ConsumerAuthEvent consumerAuthEvent;
	@Inject
	private ConsumerAuthIdentifier consumerAuthIdentifier;

	// -----------------------------------------------------
	//                                               for Business Auth
	//                                               -------
	@Inject
	private BusinessAuthEvent businessAuthEvent;
	@Inject
	private BusinessAuthIdentifier businessAuthIdentifier;

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
			.define()
				.identifier(formIdentifier)
				.base("/form/")
				.endPoint(HttpMethod.POST, "/form/auth")
				.logout("/form/logout")
				.redirect("/form")
				.requestMapper(formMapper)
				.finisher(formFinisher)
			.end()
			.define()
				.identifier(businessAuthIdentifier)
				.base("/business/")
				.endPoint(HttpMethod.POST, "/business/auth")
				.logout("/business/logout")
				.redirect("/business")
				.requestMapper(formMapper)
				.finisher(businessAuthEvent)
			.end()
			.define()
				.identifier(consumerAuthIdentifier)
				.base("/consumer/")
				.endPoint(HttpMethod.POST, "/consumer/auth")
				.logout("/consumer/logout")
				.redirect("/consumer")
				.requestMapper(formMapper)
				.finisher(consumerAuthEvent)
			.end()
			;
	}
}
