package com.epam.esm.exceptions;

public class OrderValidatorException extends RuntimeException {
    public OrderValidatorException() {
    }

    public OrderValidatorException(String message) {
        super(message);
    }

    public OrderValidatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderValidatorException(Throwable cause) {
        super(cause);
    }
}
