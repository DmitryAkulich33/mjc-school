package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.builder.CertificateQueryBuilder;
import com.epam.esm.dao.mapper.CertificateRowMapper;
import com.epam.esm.domain.Certificate;
import com.epam.esm.exceptions.CertificateDaoException;
import com.epam.esm.exceptions.CertificateDuplicateException;
import com.epam.esm.exceptions.CertificateNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Repository
public class CertificateDaoImpl implements CertificateDao {
    private final JdbcTemplate template;
    private final CertificateRowMapper rowMapper;
    private final CertificateQueryBuilder certificateQueryBuilder;

    private static final String ADD_CERTIFICATE = "INSERT INTO certificate " +
            "(name_certificate, description, price, creation_date, lock_certificate, duration) VALUES(?,?,?,?,?,?)";
    private static final String LOCK_BY_ID = "UPDATE certificate SET lock_certificate=1 WHERE id_certificate=?";
    private static final String FIND_BY_ID = "SELECT id_certificate, name_certificate, description, price, creation_date, " +
            "update_date, lock_certificate, duration FROM certificate WHERE id_certificate=? AND lock_certificate=0";
    private static final String FIND_DISTINCT_FROM_CERTIFICATES = "SELECT DISTINCT id_certificate, name_certificate, " +
            "description, price, creation_date, update_date, lock_certificate, duration FROM certificate";
    private static final String CERTIFICATE_UNLOCK = " certificate.lock_certificate=0";
    private static final String UPDATE_CERTIFICATE = "UPDATE certificate SET name_certificate=?, description=?, " +
            "price=?, update_date=?, duration=? WHERE id_certificate=?";
    private static final Integer LOCK = 0;

    @Autowired
    public CertificateDaoImpl(JdbcTemplate template, CertificateRowMapper rowMapper, CertificateQueryBuilder certificateQueryBuilder) {
        this.template = template;
        this.rowMapper = rowMapper;
        this.certificateQueryBuilder = certificateQueryBuilder;
    }

    @Override
    public Certificate createCertificate(Certificate certificate) {
        LocalDateTime creationDate = LocalDateTime.now();
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            template.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(ADD_CERTIFICATE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, certificate.getName());
                ps.setString(2, certificate.getDescription());
                ps.setDouble(3, certificate.getPrice());
                ps.setTimestamp(4, Timestamp.valueOf(creationDate));
                ps.setInt(5, LOCK);
                ps.setInt(6, certificate.getDuration());
                return ps;
            }, keyHolder);
        } catch (DuplicateKeyException e) {
            throw new CertificateDuplicateException("message.certificate.exists");
        } catch (DataAccessException e) {
            throw new CertificateDaoException("message.dao.exception");
        }

        Long idTag = Objects.requireNonNull(keyHolder.getKey()).longValue();
        certificate.setId(idTag);
        certificate.setLock(LOCK);
        certificate.setCreateDate(creationDate);
        return certificate;
    }

    @Override
    public void deleteCertificate(Long id) {
        try {
            template.update(LOCK_BY_ID, id);
        } catch (DataAccessException e) {
            throw new CertificateDaoException("message.dao.exception");
        }
    }

    @Override
    public Certificate getCertificateById(Long id) {
        try {
            return template.queryForObject(FIND_BY_ID, new Object[]{id}, rowMapper);
        } catch (DataAccessException e) {
            throw new CertificateNotFoundException("message.wrong_certificate_id");
        }
    }

    @Override
    public Certificate updateCertificate(Certificate certificate) {
        Long id = certificate.getId();
        String name = certificate.getName();
        String description = certificate.getDescription();
        double price = certificate.getPrice();
        LocalDateTime updateDate = LocalDateTime.now();
        int duration = certificate.getDuration();
        try {
            template.update(UPDATE_CERTIFICATE, name, description, price, updateDate, duration, id);
        } catch (DataAccessException e) {
            throw new CertificateDaoException("message.dao.exception");
        }
        certificate.setLastUpdateDate(updateDate);
        return certificate;
    }

    @Override
    public List<Certificate> getCertificates(String name, String search, String sort) {
        StringBuilder certificateQuery = new StringBuilder(FIND_DISTINCT_FROM_CERTIFICATES)
                .append(certificateQueryBuilder.buildTagNameQuery(name))
                .append(certificateQueryBuilder.buildSearchQuery(search))
                .append(CERTIFICATE_UNLOCK)
                .append(certificateQueryBuilder.buildSortQuery(sort));
        try {
            return template.query(certificateQuery.toString(), rowMapper);
        } catch (DataAccessException e) {
            throw new CertificateDaoException("message.dao.exception");
        }
    }
}
