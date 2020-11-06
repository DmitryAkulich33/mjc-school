package com.epam.esm.util;

import com.epam.esm.exceptions.TagValidatorException;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
public class TagValidator {

    public void validateTagId(Long id) {
        if (id == null || id <= 0) {
            throw new TagValidatorException("message.invalid_tag_id");
        }
    }

    public void validateTagName(String name) {
        if (isBlank(name)) {
            throw new TagValidatorException("message.invalid_tag_name");
        }
    }
}
