package org.carbon.sample.auth.business;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.sample.domain.service.LecturerService;
import org.carbon.web.auth.AuthIdentifier;
import org.carbon.web.exception.UserIdentityNotFoundException;

import java.util.Optional;

/**
 * @author Shota Oda 2016/11/23.
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
