package com.epam.esm.exceptions;

public class CertificateDaoException extends RuntimeException {
    public CertificateDaoException() {
    }

    public CertificateDaoException(String message) {
        super(message);
    }

    public CertificateDaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public CertificateDaoException(Throwable cause) {
        super(cause);
    }
}
