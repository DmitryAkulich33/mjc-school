package com.epam.esm.exceptions;

import lombok.*;

@Data
@AllArgsConstructor
public class ExceptionResponse {
    private String message;
    private String errorCode;

}
