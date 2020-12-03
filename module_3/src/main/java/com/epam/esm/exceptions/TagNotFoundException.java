package com.epam.esm.exceptions;

public class TagNotFoundException extends RuntimeException {
    public TagNotFoundException() {
    }

    public TagNotFoundException(String message) {
        super(message);
    }

    public TagNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TagNotFoundException(Throwable cause) {
        super(cause);
    }
}
