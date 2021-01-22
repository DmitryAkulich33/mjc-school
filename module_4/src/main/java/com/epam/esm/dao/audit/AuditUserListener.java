package com.epam.esm.dao.audit;

import com.epam.esm.domain.User;

import javax.persistence.PrePersist;

public class AuditUserListener {
    private static final Boolean DELETED = false;

    @PrePersist
    public void createUser(User user) {
        user.setDeleted(DELETED);
    }
}
