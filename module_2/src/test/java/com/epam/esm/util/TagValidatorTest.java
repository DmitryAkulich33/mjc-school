package com.epam.esm.util;

import com.epam.esm.exceptions.TagValidatorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class TagValidatorTest {
    private static final Long CORRECT_TAG_ID = 1L;
    private static final Long WRONG_TAG_ID = -1L;
    private static final String CORRECT_TAG_NAME = "name";
    private static final String WRONG_TAG_NAME = " ";

    private TagValidator tagValidator;

    @BeforeEach
    public void setUp() {
        tagValidator = new TagValidator();
    }

    @Test
    public void testValidateTagId() {
        tagValidator.validateTagId(CORRECT_TAG_ID);
    }

    @Test
    public void testValidateTagId_TagValidatorException() {
        assertThrows(TagValidatorException.class, () -> {
            tagValidator.validateTagId(WRONG_TAG_ID);
        });
    }

    @Test
    public void testValidateTagName() {
        tagValidator.validateTagName(CORRECT_TAG_NAME);
    }

    @Test
    public void testValidateTagName_TagValidatorException() {
        assertThrows(TagValidatorException.class, () -> {
            tagValidator.validateTagName(WRONG_TAG_NAME);
        });
    }
}