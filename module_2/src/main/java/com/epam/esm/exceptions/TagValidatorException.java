package com.epam.esm.exceptions;

public class TagValidatorException extends RuntimeException {
    public TagValidatorException() {
    }

    public TagValidatorException(String message) {
        super(message);
    }

    public TagValidatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public TagValidatorException(Throwable cause) {
        super(cause);
    }
}
