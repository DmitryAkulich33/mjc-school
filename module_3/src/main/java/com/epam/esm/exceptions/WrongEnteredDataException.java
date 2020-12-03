package com.epam.esm.exceptions;

public class WrongEnteredDataException extends RuntimeException {
    public WrongEnteredDataException() {
    }

    public WrongEnteredDataException(String message) {
        super(message);
    }

    public WrongEnteredDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongEnteredDataException(Throwable cause) {
        super(cause);
    }
}
