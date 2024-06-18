package io.sillysillyman.springnewsfeed.common;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidatorUtils {

    private static final ValidatorFactory factory;

    private static final Validator validator;

    static {
        Locale.setDefault(Locale.ENGLISH);
        factory = Validation.byDefaultProvider().configure()
            .buildValidatorFactory();
        validator = factory.getValidator();
    }

    public static <T> Set<ConstraintViolation<T>> validateField(T object, String propertyName) {
        return validator.validateProperty(object, propertyName);
    }

    public static <T> Set<ConstraintViolation<T>> validateFieldWithMessage(T object,
        String propertyName, String message) {
        Set<ConstraintViolation<T>> allViolations = validator.validateProperty(object,
            propertyName);
        return allViolations.stream()
            .filter(violation -> violation.getMessage().equals(message))
            .collect(Collectors.toSet());
    }

    public static Validator getValidator() {
        return validator;
    }
}