package com.fernandorobles.school.validations;

import com.fernandorobles.school.annotation.FieldsValueMatch;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class FieldsValueMatchValidator implements ConstraintValidator<FieldsValueMatch, Object> {

    private String field;
    private String fieldMatch;

    @Override
    public void initialize(FieldsValueMatch constraintAnnotation){
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context){
        Object fieldValue = new BeanWrapperImpl(value).getPropertyValue(field);
        Object fieldValueMatch = new BeanWrapperImpl(value).getPropertyValue(fieldMatch);

        return Objects.equals(fieldValue, fieldValueMatch);
    }
}
