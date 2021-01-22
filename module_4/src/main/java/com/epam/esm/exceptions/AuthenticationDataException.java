package com.epam.esm.exceptions;

public class AuthenticationDataException extends RuntimeException{
    public AuthenticationDataException() {
    }

    public AuthenticationDataException(String message) {
        super(message);
    }

    public AuthenticationDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationDataException(Throwable cause) {
        super(cause);
    }
}
