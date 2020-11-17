package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Tag;
import com.epam.esm.exceptions.TagDuplicateException;
import com.epam.esm.exceptions.TagNotFoundException;
import com.epam.esm.service.TagService;
import com.epam.esm.util.OffsetCalculator;
import com.epam.esm.util.TagValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
     * Offset's calculator for this service
     */
    private final OffsetCalculator offsetCalculator;

    /**
     * Logger for this service
     */
    private static Logger log = LogManager.getLogger(TagServiceImpl.class);

    /**
     * Constructor - creating a new object
     *
     * @param tagDao           dao for this service
     * @param tagValidator     validator for this service
     * @param offsetCalculator offset's calculator for this service
     */
    @Autowired
    public TagServiceImpl(TagDao tagDao, TagValidator tagValidator, OffsetCalculator offsetCalculator) {
        this.tagDao = tagDao;
        this.tagValidator = tagValidator;
        this.offsetCalculator = offsetCalculator;
    }

    /**
     * Create tag
     *
     * @param tag tag
     * @return tag
     */
    @Transactional
    @Override
    public Tag createTag(Tag tag) {
        log.debug("Service: creation tag.");
        String tagName = tag.getName();
        tagValidator.validateTagName(tagName);
        Optional<Tag> optionalTag = tagDao.getTagByName(tagName);
        if (optionalTag.isPresent()) {
            throw new TagDuplicateException("message.tag.exists");
        }
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
        getTagById(idTag);
        tagDao.deleteTag(idTag);
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
        Optional<Tag> tag = tagDao.getTagById(idTag);
        if (tag.isPresent()) {
            return tag.get();
        } else {
            throw new TagNotFoundException("message.wrong_tag_id");
        }
    }

    /**
     * Get all tags
     *
     * @param pageNumber page number
     * @param pageSize   page size
     * @return list of tags
     */
    @Override
    public List<Tag> getTags(Integer pageNumber, Integer pageSize) {
        log.debug("Service: search all tags.");
        Integer offset = offsetCalculator.calculateOffset(pageNumber, pageSize);
        return tagDao.getTags(offset, pageSize);
    }

    /**
     * Update tag
     *
     * @param tags list of tags
     * @return list of tags
     */
    @Transactional
    @Override
    public List<Tag> updateTags(List<Tag> tags) {
        log.debug("Service: update tags in certificate");
        Set<Tag> uniqueTags = new HashSet<>(tags);
        uniqueTags.forEach((s -> tagValidator.validateTagName(s.getName())));

        List<Tag> tagsToCreate = uniqueTags.stream()
                .filter(tag -> !tagDao.getTagByName(tag.getName()).isPresent())
                .map(tagDao::createTag)
                .collect(Collectors.toList());

        List<Tag> existingTags = uniqueTags.stream()
                .filter(tag -> !tagsToCreate.contains(tag))
                .map(tag -> tagDao.getTagByName(tag.getName()).get())
                .collect(Collectors.toList());

        existingTags.addAll(tagsToCreate);
        return existingTags;
    }
}
