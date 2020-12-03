package com.epam.esm.service;

public interface OffsetCalculator {

    default Integer calculateOffset(Integer pageNumber, Integer pageSize) {
        return (pageNumber - 1) * pageSize;
    }
}
