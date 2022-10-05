package com.fernandorobles.school.validations;

import com.fernandorobles.school.annotation.PasswordValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class PasswordStrengthValidator implements ConstraintValidator<PasswordValidator, String> {

    List<String> weakPassword;

    @Override
    public void initialize(PasswordValidator passwordValidator){
        weakPassword = Arrays.asList("12345", "password", "qwerty");
    }

    @Override
    public boolean isValid(String passwordField, ConstraintValidatorContext context){
        return passwordField != null && (!weakPassword.contains(passwordField));
    }
}
