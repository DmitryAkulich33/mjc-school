package com.epam.esm.exceptions;

public class ParseLocalDateTimeException extends RuntimeException {
    public ParseLocalDateTimeException() {
    }

    public ParseLocalDateTimeException(String message) {
        super(message);
    }

    public ParseLocalDateTimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParseLocalDateTimeException(Throwable cause) {
        super(cause);
    }
}
