package br.com.daniel.java.quarkus.general.utils.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class ValidadorIntervaloDatas implements ConstraintValidator<IntervaloValido, Object> {

    private String fieldStartDate;
    private String fieldEndDate;
    private String message;

    @Override
    public void initialize(IntervaloValido constraintAnnotation) {
        this.fieldStartDate = constraintAnnotation.fieldStartDate();
        this.fieldEndDate = constraintAnnotation.fieldEndDate();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        if (object == null) {
            return true;
        }

        try {
            Object initValue = getFieldValue(object, fieldStartDate);
            Object endValue = getFieldValue(object, fieldEndDate);

            // Se algum dos campos for nulo, deixa outras anotações (ex: @NotNull) tratarem
            if (initValue == null || endValue == null) {
                return true;
            }

            boolean valido = true;

            // Suporte para Comparable (LocalDateTime, LocalDate, Instant, Date, etc.)
            if (initValue instanceof Comparable && endValue instanceof Comparable) {
                @SuppressWarnings("unchecked")
                Comparable<Object> inicio = (Comparable<Object>) initValue;
                valido = inicio.compareTo(endValue) < 0;
            }

            if (!valido) {
                // Direciona a mensagem de erro especificamente para o campo final no JSON de resposta
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message)
                        .addPropertyNode(fieldEndDate)
                        .addConstraintViolation();
            }

            return valido;

        } catch (Exception e) {
            // Em caso de erro ao acessar os atributos por Reflection
            return false;
        }
    }

    private Object getFieldValue(Object object, String fieldName) throws Exception {
        Class<?> clazz = object.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(object);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass(); // Busca na superclasse caso haja herança
            }
        }

        throw new NoSuchFieldException("Campo '" + fieldName + "' não encontrado na classe " + object.getClass().getName());
    }
}
