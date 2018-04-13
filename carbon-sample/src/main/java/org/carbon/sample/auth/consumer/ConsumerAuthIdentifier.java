package org.carbon.sample.auth.consumer;

import java.util.Optional;

import org.carbon.authentication.AuthIdentifier;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Assemble;
import org.carbon.sample.domain.service.StudentService;

/**
 * @author Shota Oda 2016/11/23.
 */
@Component
public class ConsumerAuthIdentifier implements AuthIdentifier<ConsumerAuthIdentity> {
    @Assemble
    private StudentService service;

    @Override
    public Optional<ConsumerAuthIdentity> identify(String identity) {
        return service.selectOneByAddress(identity)
                .map(ConsumerAuthIdentity::new);
    }
}
