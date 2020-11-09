package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Tag;
import com.epam.esm.exceptions.TagDaoException;
import com.epam.esm.service.TagService;
import com.epam.esm.util.TagValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {
    /**
     * Dao for this server
     */
    private final TagDao tagDao;

    /**
     * Validator for this service
     */
    private final TagValidator tagValidator;

    /**
     * Logger for this service
     */
    private static Logger log = LogManager.getLogger(TagServiceImpl.class);

    /**
     * Constructor - creating a new object
     *
     * @param tagDao       dao for this server
     * @param tagValidator validator for this service
     */
    @Autowired
    public TagServiceImpl(TagDao tagDao, TagValidator tagValidator) {
        this.tagDao = tagDao;
        this.tagValidator = tagValidator;
    }

    /**
     * Create tag
     *
     * @param tag tag
     * @return tag
     */
    @Override
    public Tag createTag(Tag tag) {
        log.debug("Service: creation tag.");
        tagValidator.validateTagName(tag.getName());

        return tagDao.createTag(tag);
    }

    /**
     * Delete tag
     *
     * @param idTag tag's id
     */
    @Transactional
    @Override
    public void deleteTag(Long idTag) {
        log.debug(String.format("Service: deletion tag by id  %d", idTag));
        tagValidator.validateTagId(idTag);
        tagDao.getTagById(idTag);
        int result = tagDao.deleteTag(idTag);
        if (result == 0) {
            throw new TagDaoException("message.dao.exception");
        }
    }

    /**
     * Get tag by id
     *
     * @param idTag tag's id
     * @return tag
     */
    @Override
    public Tag getTagById(Long idTag) {
        log.debug(String.format("Service: search tag by id %d", idTag));
        tagValidator.validateTagId(idTag);
        return tagDao.getTagById(idTag);
    }

    /**
     * Get all tags
     *
     * @return list of tags
     */
    @Override
    public List<Tag> getAllTags() {
        log.debug("Service: search all tags.");
        return tagDao.getAllTags();
    }

    /**
     * Update tag
     *
     * @param tags          list of tags
     * @param idCertificate certificate's id
     */
    @Transactional
    @Override
    public void updateTags(List<Tag> tags, Long idCertificate) {
        log.debug(String.format("Service: update tags in certificate with %d", idCertificate));
        List<Tag> tagsFromDB = tagDao.getAllTags();
        Set<Tag> uniqueTags = new HashSet<>(tags);
        for (Tag tag : uniqueTags) {
            String nameTag = tag.getName();
            if (isNewTag(tagsFromDB, nameTag)) {
                Tag tagToCreate = new Tag();
                tagToCreate.setName(nameTag);
                Tag createdTag = tagDao.createTag(tagToCreate);
                Long idTag = createdTag.getId();
                tagDao.createTagCertificate(idTag, idCertificate);
            } else {
                Tag currentTag = tagDao.getTagByName(nameTag);
                Long idTag = currentTag.getId();
                List<Tag> tagsCertificateFromDB = tagDao.getCertificateTags(idCertificate);
                if (!isTagBelongToCertificate(tagsCertificateFromDB, idTag)) {
                    tagDao.createTagCertificate(idTag, idCertificate);
                }
            }
        }
    }

    /**
     * Determine if a tag is new or not
     *
     * @param tags    list of tags
     * @param nameTag tag's name
     */
    @Override
    public boolean isNewTag(List<Tag> tags, String nameTag) {
        log.debug("Service: check is tag new.");
        List<Tag> list = tags.stream()
                .filter(x -> x.getName().equals(nameTag))
                .collect(Collectors.toList());

        return list.size() == 0;
    }

    private boolean isTagBelongToCertificate(List<Tag> tags, Long idTag) {

        return tags.stream().anyMatch(tag -> tag.getId().equals(idTag));
    }
}
