package com.epam.esm.util;

import com.epam.esm.exceptions.PaginationValidatorException;
import org.springframework.stereotype.Component;

@Component
public class PaginationValidator {

    public void validatePagination(Integer pageNumber, Integer pageSize) {
        validatePageNumber(pageNumber);
        validatePageSize(pageSize);
    }

    private void validatePageNumber(Integer pageNumber) {
        if (pageNumber == null || pageNumber <= 0) {
            throw new PaginationValidatorException("message.invalid_page_number");
        }
    }

    private void validatePageSize(Integer pageSize) {
        if (pageSize == null || pageSize <= 0) {
            throw new PaginationValidatorException("message.invalid_page_size");
        }
    }
}
