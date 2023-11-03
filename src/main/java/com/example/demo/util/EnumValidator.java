package com.example.demo.util;

import com.example.demo.common.annotaction.ValidEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;

public class EnumValidator implements ConstraintValidator<ValidEnum, Enum<?>> {

  private ValidEnum annotation;

  @Override
  public void initialize(ValidEnum constraintAnnotation) {
    this.annotation = constraintAnnotation;
  }

  @Override
  public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
    boolean result = false;
    Object[] enumValues = this.annotation.enumClass().getEnumConstants();
    if (!Objects.isNull(enumValues)) {
      for (Object enumValue : enumValues) {
        if (value == enumValue) {
          result = true;
          break;
        }
      }
    }
    return result;
  }
}
