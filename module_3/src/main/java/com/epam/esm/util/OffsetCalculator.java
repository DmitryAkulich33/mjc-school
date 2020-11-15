package com.epam.esm.util;

import org.springframework.stereotype.Component;

@Component
public class OffsetCalculator {

    public Integer calculate(int pageNumber, int pageSize) {
        return (pageNumber - 1) * pageSize;
    }
}
