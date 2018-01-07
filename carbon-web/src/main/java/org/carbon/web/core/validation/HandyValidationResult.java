package org.carbon.web.core.validation;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;

/**
 * @author Shota Oda 2016/12/11.
 */
public class HandyValidationResult extends ValidationResult {
    private Map<String, String> simpleViolationResults;

    public HandyValidationResult(Set<ConstraintViolation> constraintViolations) {
        super(constraintViolations);
        parseSimpleMap();
    }

    public boolean existErrorFor(String key) {
        return simpleViolationResults.containsKey(key);
    }

    public Map<String, String> getViolationResults() {
        return simpleViolationResults;
    }

    private void parseSimpleMap() {
        simpleViolationResults = this.constraintViolations.stream()
                .collect(Collectors.toMap(
                        cv -> cv.getPropertyPath().toString(),
                        ConstraintViolation::getMessage
                ));
    }
}
