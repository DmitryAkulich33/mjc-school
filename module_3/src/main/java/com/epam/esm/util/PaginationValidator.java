package com.epam.esm.util;

import com.epam.esm.exceptions.PaginationValidatorException;
import org.springframework.stereotype.Component;

@Component
public class PaginationValidator {

    public void validatePagination(Integer pageNumber, Integer pageSize) {
        validatePageNumber(pageNumber, pageSize);
        validatePageSize(pageNumber, pageSize);
    }

    private void validatePageNumber(Integer pageNumber, Integer pageSize) {
        if(pageNumber != null) {
            if (pageSize == null) {
                throw new PaginationValidatorException("message.invalid_page_number");
            } else if (pageNumber <= 0){
                throw new PaginationValidatorException("message.invalid_page_number");
            }
        }
    }

    private void validatePageSize(Integer pageNumber, Integer pageSize) {
        if(pageSize != null) {
            if (pageNumber == null) {
                throw new PaginationValidatorException("message.invalid_page_number");
            } else if (pageSize <= 0){
                throw new PaginationValidatorException("message.invalid_page_number");
            }
        }
    }
}
