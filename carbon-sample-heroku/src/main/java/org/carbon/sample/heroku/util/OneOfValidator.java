package org.carbon.sample.heroku.util;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OneOfValidator implements ConstraintValidator<OneOf, String> {

    private Set<String> AVAILABLE_VALUES;

    @Override
    public void initialize(OneOf oneOf) {
        Class<? extends Enum<?>> enumClass = oneOf.value();
        AVAILABLE_VALUES = getNamesSet(enumClass);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || AVAILABLE_VALUES.contains(value);
    }

    private Set<String> getNamesSet(Class<? extends Enum<?>> e) {
        Enum<?>[] enums = e.getEnumConstants();
        return Stream.of(enums)
                .map(Enum::name)
                .collect(Collectors.toSet());
    }
}