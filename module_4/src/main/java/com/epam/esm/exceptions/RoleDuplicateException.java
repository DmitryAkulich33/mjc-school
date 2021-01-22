package com.epam.esm.exceptions;

public class RoleDuplicateException extends RuntimeException{
    public RoleDuplicateException() {
    }

    public RoleDuplicateException(String message) {
        super(message);
    }

    public RoleDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public RoleDuplicateException(Throwable cause) {
        super(cause);
    }
}
