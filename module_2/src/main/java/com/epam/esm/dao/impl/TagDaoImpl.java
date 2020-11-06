package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.mapper.TagRowMapper;
import com.epam.esm.domain.Tag;
import com.epam.esm.exceptions.CertificateNotFoundException;
import com.epam.esm.exceptions.TagDaoException;
import com.epam.esm.exceptions.TagDuplicateException;
import com.epam.esm.exceptions.TagNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class TagDaoImpl implements TagDao {
    private final JdbcTemplate template;
    private final TagRowMapper rowMapper;

    private static final String ADD_TAG = "INSERT INTO tag (name_tag, lock_tag) VALUES(?, ?)";
    private static final String LOCK_BY_ID = "UPDATE tag SET lock_tag=1 WHERE id_tag=?";
    private static final String FIND_BY_ID = "SELECT id_tag, name_tag, lock_tag FROM tag " +
            "WHERE id_tag=? AND lock_tag=0";
    private static final String FIND_BY_NAME = "SELECT id_tag, name_tag, lock_tag FROM tag " +
            "WHERE name_tag=? AND lock_tag=0";
    private static final String FIND_ALL = "SELECT id_tag, name_tag, lock_tag FROM tag " +
            "WHERE lock_tag=0";
    private static final String ADD_TAG_CERTIFICATE = "INSERT INTO tag_certificate " +
            "(tag_id, certificate_id) VALUES(?,?)";
    private static final String FIND_TAGS_CERTIFICATE = "SELECT id_tag, name_tag, lock_tag FROM tag " +
            "JOIN tag_certificate ON tag.id_tag=tag_certificate.tag_id WHERE certificate_id=?";
    private static final Integer LOCK = 0;

    @Autowired
    public TagDaoImpl(JdbcTemplate template, TagRowMapper rowMapper) {
        this.template = template;
        this.rowMapper = rowMapper;
    }

    @Override
    public Tag createTag(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            template.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(ADD_TAG, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, tag.getName());
                ps.setInt(2, LOCK);
                return ps;
            }, keyHolder);
        } catch (DuplicateKeyException e) {
            throw new TagDuplicateException("message.tag.exists");
        } catch (DataAccessException e) {
            throw new TagDaoException("message.dao.exception");
        }

        Long idTag = Objects.requireNonNull(keyHolder.getKey()).longValue();
        tag.setId(idTag);
        tag.setLock(LOCK);
        return tag;
    }

    @Override
    public void createTagCertificate(Long idTag, Long idCertificate) {
        try {
            template.update(ADD_TAG_CERTIFICATE, idTag, idCertificate);
        } catch (DataAccessException e) {
            throw new CertificateNotFoundException("message.wrong_certificate_id");
        }
    }

    @Override
    public void deleteTag(Long id) {
        try {
            template.update(LOCK_BY_ID, id);
        } catch (DataAccessException e) {
            throw new TagDaoException("message.dao.exception");
        }
    }

    @Override
    public Tag getTagById(Long id) {
        try {
            return template.queryForObject(FIND_BY_ID, new Object[]{id}, rowMapper);
        } catch (DataAccessException e) {
            throw new TagNotFoundException("message.wrong_tag_id");
        }
    }

    @Override
    public Tag getTagByName(String name) {
        try {
            return template.queryForObject(FIND_BY_NAME, new Object[]{name}, rowMapper);
        } catch (DataAccessException e) {
            throw new TagNotFoundException("message.wrong_tag_name");
        }
    }

    @Override
    public List<Tag> getAllTags() {
        try {
            return template.query(FIND_ALL, rowMapper);
        } catch (DataAccessException e) {
            throw new TagDaoException("message.dao.exception");
        }
    }

    @Override
    public List<Tag> getCertificateTags(Long idCertificate) {
        try {
            return template.query(FIND_TAGS_CERTIFICATE, rowMapper, idCertificate);
        } catch (DataAccessException e) {
            throw new TagDaoException("message.dao.exception");
        }
    }
}
