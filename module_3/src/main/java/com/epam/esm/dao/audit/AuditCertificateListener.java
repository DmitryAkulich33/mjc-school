package com.epam.esm.dao.audit;

import com.epam.esm.domain.Certificate;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class AuditCertificateListener {
    private static final Integer LOCK = 0;

    @PrePersist
    public void createCertificate(Certificate certificate) {
        setCreateDate(certificate);
        certificate.setLock(LOCK);
    }

    @PreUpdate
    public void updateCertificate(Certificate certificate) {
        setUpdateDate(certificate);
    }

    private void setCreateDate(Certificate certificate) {
        LocalDateTime creationDate = LocalDateTime.now();
        certificate.setCreateDate(creationDate);
    }

    private void setUpdateDate(Certificate certificate) {
        LocalDateTime updateDate = LocalDateTime.now();
        certificate.setLastUpdateDate(updateDate);
    }
}
