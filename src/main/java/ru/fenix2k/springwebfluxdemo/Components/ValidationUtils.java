package ru.fenix2k.springwebfluxdemo.Components;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import ru.fenix2k.springwebfluxdemo.Exceptions.ValidationException;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
@Scope("singleton")
@AllArgsConstructor
public class ValidationUtils {

    private Validator validator;

    public boolean validate(Object model) {

        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(model);

        if (!CollectionUtils.isEmpty(constraintViolations)) {
            Map<String, String> errors = new HashMap<>();
            constraintViolations.forEach(
                    modelConstraintViolation ->
                            errors.put(modelConstraintViolation.getPropertyPath().toString(), modelConstraintViolation.getMessage()));
            throw new ValidationException(errors);
        }

        return true;
    }

}
