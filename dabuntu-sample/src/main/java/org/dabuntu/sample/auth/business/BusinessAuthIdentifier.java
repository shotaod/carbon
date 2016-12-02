package org.dabuntu.sample.auth.business;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Inject;
import org.dabuntu.sample.domain.service.LecturerService;
import org.dabuntu.web.auth.AuthIdentifier;
import org.dabuntu.web.exception.UserIdentityNotFoundException;

import java.util.Optional;

/**
 * @author ubuntu 2016/11/23.
 */
@Component
public class BusinessAuthIdentifier implements AuthIdentifier<BusinessAuthIdentity>{

	@Inject
	private LecturerService service;

	@Override
	public Class<BusinessAuthIdentity> getType() {
		return BusinessAuthIdentity.class;
	}

	@Override
	public BusinessAuthIdentity find(String address) throws UserIdentityNotFoundException {
		return Optional.ofNullable(service.findByAddress(address))
				.map(BusinessAuthIdentity::new)
				.orElseThrow(() -> new UserIdentityNotFoundException(address));
	}
}
