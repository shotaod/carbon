package org.carbon.sample.auth.business;

import java.util.Optional;

import org.carbon.authentication.AuthIdentifier;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.sample.domain.service.LecturerService;

/**
 * @author Shota Oda 2016/11/23.
 */
@Component
public class BusinessAuthIdentifier implements AuthIdentifier<BusinessAuthIdentity> {

    @Inject
    private LecturerService service;

    @Override
    public Optional<BusinessAuthIdentity> find(String identity) {
        return Optional.ofNullable(service.findByAddress(identity))
                .map(BusinessAuthIdentity::new);
    }
}
