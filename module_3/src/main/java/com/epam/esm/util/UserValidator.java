package com.epam.esm.util;

import com.epam.esm.exceptions.UserValidatorException;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    public void validateUserId(Long id) {
        if (id == null || id <= 0) {
            throw new UserValidatorException("message.invalid_user_id");
        }
    }
}
