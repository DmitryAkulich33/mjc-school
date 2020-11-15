package com.epam.esm.dao.audit;

import com.epam.esm.domain.Certificate;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class AuditCertificateListener {
    private static final Integer LOCK = 0;

    @PrePersist
    public void createCertificate(Certificate certificate) {
        setDate(certificate);
        certificate.setLock(LOCK);
    }

    @PreUpdate
    public void updateCertificate(Certificate certificate) {
        setDate(certificate);
    }

    private void setDate(Certificate certificate) {
        LocalDateTime creationDate = LocalDateTime.now();
        certificate.setCreateDate(creationDate);
    }
}
