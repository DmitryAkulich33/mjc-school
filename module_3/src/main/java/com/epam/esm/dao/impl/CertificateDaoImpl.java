package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.builder.CertificateQueryBuilder;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CertificateDaoImpl implements CertificateDao {
    @PersistenceContext
    private final EntityManager entityManager;
    private final CertificateQueryBuilder certificateQueryBuilder;

//    private static final String ADD_CERTIFICATE = "INSERT INTO certificate " +
//            "(name_certificate, description, price, creation_date, lock_certificate, duration) VALUES(?,?,?,?,?,?)";
//
//    private static final String UPDATE_CERTIFICATE = "UPDATE certificate SET name_certificate=?, description=?, " +
//            "price=?, update_date=?, duration=? WHERE id_certificate=?";
    private static final Integer LOCK = 0;

    @Autowired
    public CertificateDaoImpl(EntityManager entityManager, CertificateQueryBuilder certificateQueryBuilder) {
        this.entityManager = entityManager;
        this.certificateQueryBuilder = certificateQueryBuilder;
    }

    @Transactional
    @Override
    public Certificate createCertificate(Certificate certificate) {
        LocalDateTime creationDate = LocalDateTime.now();
        certificate.setLock(LOCK);
        certificate.setCreateDate(creationDate);
        entityManager.persist(certificate);
        return certificate;
        //        LocalDateTime creationDate = LocalDateTime.now();
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//
//        try {
//            template.update(connection -> {
//                PreparedStatement ps = connection
//                        .prepareStatement(ADD_CERTIFICATE, Statement.RETURN_GENERATED_KEYS);
//                ps.setString(1, certificate.getName());
//                ps.setString(2, certificate.getDescription());
//                ps.setDouble(3, certificate.getPrice());
//                ps.setTimestamp(4, Timestamp.valueOf(creationDate));
//                ps.setInt(5, LOCK);
//                ps.setInt(6, certificate.getDuration());
//                return ps;
//            }, keyHolder);
//        } catch (DuplicateKeyException e) {
//            throw new CertificateDuplicateException("message.certificate.exists");
//        } catch (DataAccessException e) {
//            throw new CertificateDaoException("message.dao.exception");
//        }
//
//        Long idTag = Objects.requireNonNull(keyHolder.getKey()).longValue();
//        certificate.setId(idTag);
//        certificate.setLock(LOCK);
//        certificate.setCreateDate(creationDate);
//        return certificate;
    }

    @Override
    public int deleteCertificate(Long id) {
        Query certificateQuery = entityManager.createNamedQuery(Certificate.QueryNames.LOCK_BY_ID);
        certificateQuery.setParameter("idCertificate", id);

        return certificateQuery.executeUpdate();
    }

    @Override
    public Certificate getCertificateById(Long id) {
        TypedQuery<Certificate> certificateQuery = entityManager.createNamedQuery(Certificate.QueryNames.FIND_BY_ID, Certificate.class);
        certificateQuery.setParameter("idCertificate", id);

        return certificateQuery.getSingleResult();
    }

    @Override
    public Certificate updateCertificate(Certificate certificate) {
//        Long id = certificate.getId();
//        String name = certificate.getName();
//        String description = certificate.getDescription();
//        double price = certificate.getPrice();
//        LocalDateTime updateDate = LocalDateTime.now();
//        int duration = certificate.getDuration();
//        try {
//            template.update(UPDATE_CERTIFICATE, name, description, price, updateDate, duration, id);
//        } catch (DataAccessException e) {
//            throw new CertificateDaoException("message.dao.exception");
//        }
//        certificate.setLastUpdateDate(updateDate);
        return certificate;
    }

    @Override
    public List<Certificate> getCertificates(String name, String search, String sort) {
        String query = certificateQueryBuilder.buildCertificatesQuery(name, search, sort);
        Query certificatesQuery = entityManager.createNativeQuery(query, Certificate.class);
        List<Certificate> certificates = certificatesQuery.getResultList();
        return certificates;
    }
}
