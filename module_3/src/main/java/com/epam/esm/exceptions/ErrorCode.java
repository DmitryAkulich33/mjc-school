package com.epam.esm.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    TAG_ERROR_CODE("01"),
    CERTIFICATE_ERROR_CODE("02"),
    ORDER_ERROR_CODE("03"),
    USER_ERROR_CODE("04"),
    PAGINATION_ERROR_CODE("05"),
    DATA_ERROR_CODE("04"),
    VALIDATE_ERROR_CODE("01"),
    DAO_ERROR_CODE("02"),
    PARSE_ERROR_CODE("03");

    private final String errorCode;
}
