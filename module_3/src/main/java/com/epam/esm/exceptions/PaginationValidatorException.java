package com.epam.esm.exceptions;

public class PaginationValidatorException extends RuntimeException {
    public PaginationValidatorException() {
    }

    public PaginationValidatorException(String message) {
        super(message);
    }

    public PaginationValidatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaginationValidatorException(Throwable cause) {
        super(cause);
    }
}
