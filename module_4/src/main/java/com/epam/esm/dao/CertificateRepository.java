package com.epam.esm.dao;

import com.epam.esm.domain.Certificate;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRepository extends CommonEntityRepository<Certificate>, JpaSpecificationExecutor<Certificate> {
}
