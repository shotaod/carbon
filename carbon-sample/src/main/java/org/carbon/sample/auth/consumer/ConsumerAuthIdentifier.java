package org.carbon.sample.auth.consumer;

import org.carbon.sample.domain.service.StudentService;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.web.auth.AuthIdentifier;
import org.carbon.web.exception.UserIdentityNotFoundException;

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
