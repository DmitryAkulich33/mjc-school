package com.epam.esm.util;

import org.springframework.stereotype.Component;

@Component
public class OffsetCalculator {

    public Integer calculateOffset(Integer pageNumber, Integer pageSize) {
        return (pageNumber - 1) * pageSize;
    }
}
