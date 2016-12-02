package org.dabuntu.sample.auth.consumer;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Inject;
import org.dabuntu.sample.domain.service.StudentService;
import org.dabuntu.web.auth.AuthIdentifier;
import org.dabuntu.web.exception.UserIdentityNotFoundException;

/**
 * @author ubuntu 2016/11/23.
 */
@Component
public class ConsumerAuthIdentifier implements AuthIdentifier<ConsumerAuthIdentity>{
	@Inject
	private StudentService service;
	@Override
	public Class<ConsumerAuthIdentity> getType() {
		return ConsumerAuthIdentity.class;
	}

	@Override
	public ConsumerAuthIdentity find(String address) throws UserIdentityNotFoundException {
		return service.selectOneByAddress(address)
				.map(ConsumerAuthIdentity::new)
				.orElseThrow(() -> new UserIdentityNotFoundException(address));
	}
}
