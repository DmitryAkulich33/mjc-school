package com.epam.esm.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class OffsetCalculatorTest {
    private static final Integer PAGE_NUMBER = 1;
    private static final Integer PAGE_SIZE = 10;
    private static final Integer OFFSET_RIGHT = 0;
    private static final Integer OFFSET_WRONG = 1;

    private OffsetCalculator offsetCalculator;

    @BeforeEach
    public void setUp() {
        offsetCalculator = new OffsetCalculator();
    }

    @Test
    public void testCalculateOffset() {
        Integer expected = offsetCalculator.calculateOffset(PAGE_NUMBER, PAGE_SIZE);

        assertEquals(expected, OFFSET_RIGHT);
    }

    @Test
    public void testCalculateOffset_WrongResult() {
        Integer expected = offsetCalculator.calculateOffset(PAGE_NUMBER, PAGE_SIZE);

        assertNotEquals(expected, OFFSET_WRONG);
    }


}