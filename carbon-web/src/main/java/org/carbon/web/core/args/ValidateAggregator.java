package org.carbon.web.core.args;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.web.annotation.Validate;
import org.carbon.web.container.ArgumentMeta;
import org.carbon.web.core.validation.ValidationResult;
import org.carbon.web.exception.BadRequestException;
import org.carbon.web.exception.WrappedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota.Oda 2018/02/22.
 */
@Component
public class ValidateAggregator implements ArgumentAggregatorAfter<Validate> {
    private static final Logger logger = LoggerFactory.getLogger(ValidateAggregator.class);

    public static class ValidationFailureException extends BadRequestException {
        public ValidationFailureException(String message) {
            super(message);
        }
    }

    @Assemble
    private Validator validator;

    @Override
    public Class<Validate> target() {
        return Validate.class;
    }

    @Override
    public ArgumentMeta aggregate(ArgumentMeta meta, HttpServletRequest request, Parameter nextParameter) {
        Object targetValue = meta.getValue();
        if (targetValue == null) {
            throw WrappedException.wrap(new ValidationFailureException("value must not be null"));
        }
        Set<ConstraintViolation<Object>> violations = validator.validate(targetValue);
        boolean existViolation = !violations.isEmpty();
        Class<? extends ValidationResult> holderType = getViolationHolderClassIfExist(nextParameter);

        // logging for debug
        if (existViolation && logger.isDebugEnabled()) {
            String violationMessage = violations.stream()
                    .map(violation -> String.format("%s: %s", violation.getPropertyPath(), violation.getMessage()))
                    .collect(Collectors.joining(",", "{", "}"));
            logger.debug("detect violation, {}", violationMessage);
        }
        // exist violation holder
        if (holderType != null) {
            try {
                Constructor<? extends ValidationResult> constructor = holderType.getConstructor(Set.class);
                ValidationResult result = constructor.newInstance(violations);
                return new ArgumentMeta(nextParameter, result);
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ignore) {
                throw WrappedException.wrap(new ValidationFailureException(""));
            }
        }

        // no holder x no violation
        if (!existViolation) {
            return null;
        }
        // no holder x exist violation
        throw WrappedException.wrap(new ValidationFailureException(""));
    }

    private Class<? extends ValidationResult> getViolationHolderClassIfExist(Parameter param) {
        if (param == null) {
            return null;
        }
        Class<?> type = param.getType();
        if (!ValidationResult.class.isAssignableFrom(type)) {
            return null;
        }
        @SuppressWarnings("unchecked")
        Class<? extends ValidationResult> vType = (Class<? extends ValidationResult>) type;
        return vType;
    }
}
