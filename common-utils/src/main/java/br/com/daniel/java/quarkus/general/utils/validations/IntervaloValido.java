package br.com.daniel.java.quarkus.general.utils.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidadorIntervaloDatas.class)
@Documented
public @interface IntervaloValido {

    String message() default "A data final deve ser posterior à data inicial";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Nome do campo contendo a data inicial
     */
    String fieldStartDate() default "startDate";

    /**
     * Nome do campo contendo a data final
     */
    String fieldEndDate() default "endDate";
}
