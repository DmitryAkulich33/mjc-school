package com.epam.esm.dao.audit;

import com.epam.esm.domain.Tag;

import javax.persistence.PrePersist;

public class AuditTagListener {
    private static final Integer LOCK = 0;

    @PrePersist
    public void createTag(Tag tag) {
        tag.setLock(LOCK);
    }
}
