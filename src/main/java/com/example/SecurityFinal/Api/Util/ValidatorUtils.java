package com.example.SecurityFinal.Api.Util;

import com.example.SecurityFinal.Api.Exception.ApiRequestException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

public class ValidatorUtils {


        public static <T, D> void validateAndApplyFields(
                T entity,
                D dtoInstance,
                Map<String, Object> values,
                Class<T> entityClass,
                Class<D> dtoClass
        ) throws MethodArgumentNotValidException, IllegalArgumentException {

            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            BindingResult bindingResult = new BeanPropertyBindingResult(entity, entityClass.getSimpleName());

            for (Map.Entry<String, Object> entry : values.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                D dto = dtoInstance;
                Field dtoField = ReflectionUtils.findField(dtoClass, key);

                if (dtoField != null) {
                    dtoField.setAccessible(true);
                    ReflectionUtils.setField(dtoField, dto, value);

                    Set<ConstraintViolation<D>> violations = validator.validateProperty(dto, key);

                    if (!violations.isEmpty()) {
                        violations.forEach(violation -> bindingResult.rejectValue(
                                key,
                                violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName(),
                                violation.getMessage()
                        ));
                    }

                    if (violations.isEmpty()) {
                        Field entityField = ReflectionUtils.findField(entityClass, key);
                        if (entityField != null) {
                            entityField.setAccessible(true);
                            ReflectionUtils.setField(entityField, entity, value);
                        }
                    }
                } else {
                    throw  ApiRequestException.badRequest("Field " + key + " does not exist in " + dtoClass.getSimpleName());
                }
            }

            if (bindingResult.hasErrors()) {
                throw new MethodArgumentNotValidException(null, bindingResult);
            }
    }

}
