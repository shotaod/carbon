package org.dabuntu.web.core.validation;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * @author ubuntu 2016/12/11.
 */
public class ValidationResult {
    protected Set<ConstraintViolation> constraintViolations;
    public ValidationResult(Set<ConstraintViolation> constraintViolations) {
        this.constraintViolations = constraintViolations;
    }
    public Set<ConstraintViolation> getConstraintViolations() {
        return constraintViolations;
    }
    public boolean existError() {
        return !constraintViolations.isEmpty();
    }
}
