package com.example.LibraryManager.models.entity;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;


public class AuthorTest {

    @Test
    public void testAuthorConstraints_ShouldFailOnBlankNameViolation() {

        Author author = new Author();

        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();

        Set<ConstraintViolation<Author>> violations = validator.validate(author);
        assertFalse(violations.isEmpty());
    }
}
