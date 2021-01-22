package com.epam.esm.exceptions;

public class OrderDaoException extends RuntimeException {
    public OrderDaoException() {
    }

    public OrderDaoException(String message) {
        super(message);
    }

    public OrderDaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderDaoException(Throwable cause) {
        super(cause);
    }
}
