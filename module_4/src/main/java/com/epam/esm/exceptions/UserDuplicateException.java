package com.epam.esm.exceptions;

public class UserDuplicateException extends RuntimeException {
    public UserDuplicateException() {
    }

    public UserDuplicateException(String message) {
        super(message);
    }

    public UserDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserDuplicateException(Throwable cause) {
        super(cause);
    }
}
