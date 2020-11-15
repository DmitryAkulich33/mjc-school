package com.epam.esm.util;

import com.epam.esm.exceptions.UserValidatorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UserValidatorTest {
    private static final Long CORRECT_USER_ID = 1L;
    private static final Long WRONG_USER_ID = -1L;

    private UserValidator userValidator;

    @BeforeEach
    public void setUp() {
        userValidator = new UserValidator();
    }

    @Test
    public void testValidateUserId() {
        userValidator.validateUserId(CORRECT_USER_ID);
    }

    @Test
    public void testValidateUserId_UserValidatorException() {
        assertThrows(UserValidatorException.class, () -> {
            userValidator.validateUserId(WRONG_USER_ID);
        });
    }
}