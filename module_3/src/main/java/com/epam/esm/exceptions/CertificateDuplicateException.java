package com.epam.esm.exceptions;

public class CertificateDuplicateException extends RuntimeException {
    public CertificateDuplicateException() {
    }

    public CertificateDuplicateException(String message) {
        super(message);
    }

    public CertificateDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public CertificateDuplicateException(Throwable cause) {
        super(cause);
    }
}
