package com.azimbabu.javapersistence.ch03.validation;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class ModelValidation {

  @Test
  void validateItem() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    Item item = new Item();
    item.setName("Some Item");
    item.setAuctionEnd(new Date());

    Set<ConstraintViolation<Item>> violations = validator.validate(item);
    ConstraintViolation<Item> violation = violations.iterator().next();
    String failedPropertyName = violation.getPropertyPath().iterator().next().getName();

    // Validation error, auction end date was not in the future!
    assertAll(() -> assertEquals(1, violations.size()),
        () -> assertEquals("auctionEnd", failedPropertyName),
        () -> {
          if (Locale.getDefault().getLanguage().equals("en")) {
            assertEquals("must be a future date", violation.getMessage());
          }
        });
  }
}
