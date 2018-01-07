package org.carbon.sample.v2.util;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = OneOfValidator.class)
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
public @interface OneOf {

    Class<? extends Enum<?>> value();
    String message() default "{org.carbon.sample.v2.util.OneOf.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}