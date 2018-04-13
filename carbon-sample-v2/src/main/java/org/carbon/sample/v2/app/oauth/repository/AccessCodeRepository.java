package org.carbon.sample.v2.app.oauth.repository;

import java.util.ArrayList;
import java.util.List;

import org.carbon.component.annotation.Component;

/**
 * @author Shota Oda 2017/08/11.
 */
@Component
public class AccessCodeRepository {

    List<AccessCode> accessCodes;

    public void save(AccessCode code) {
        if (accessCodes == null) {
            accessCodes = new ArrayList<>();
        }

        for (AccessCode accessCode : accessCodes) {
            if (!accessCode.isValid()) {
                accessCodes.remove(accessCode);
            }
        }

        accessCodes.add(code);
    }

    public AccessCode find(String host, String code) {
        if (accessCodes == null) {
            return null;
        }

        for (AccessCode accessCode : accessCodes) {
            if (accessCode.getHost().equals(host) && accessCode.getCode().equals(code)) {
                boolean valid = accessCode.isValid();
                if (!valid) {
                    accessCodes.remove(accessCode);
                }
                return accessCode;
            }
        }

        return null;
    }
}
