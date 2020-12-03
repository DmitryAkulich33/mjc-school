package com.epam.esm.dao.audit;

import com.epam.esm.domain.User;

import javax.persistence.PrePersist;

public class AuditUserListener {
    private static final Integer LOCK = 0;

    @PrePersist
    public void createUser(User user) {
        user.setLock(LOCK);
    }
}
