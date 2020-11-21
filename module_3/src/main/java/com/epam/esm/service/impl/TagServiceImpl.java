package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Tag;
import com.epam.esm.exceptions.TagNotFoundException;
import com.epam.esm.service.TagService;
import com.epam.esm.util.PaginationValidator;
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
    private final TagDao tagDao;
    private final TagValidator tagValidator;
    private final PaginationValidator paginationValidator;
    private static Logger log = LogManager.getLogger(TagServiceImpl.class);

    @Autowired
    public TagServiceImpl(TagDao tagDao, TagValidator tagValidator, PaginationValidator paginationValidator) {
        this.tagDao = tagDao;
        this.tagValidator = tagValidator;
        this.paginationValidator = paginationValidator;
    }

    @Transactional
    @Override
    public Tag createTag(Tag tag) {
        log.debug("Service: creation tag.");
        String tagName = tag.getName();
        tagValidator.validateTagName(tagName);
        return tagDao.createTag(tag);
    }

    @Transactional
    @Override
    public void deleteTag(Long idTag) {
        log.debug(String.format("Service: deletion tag by id  %d", idTag));
        getTagById(idTag);
        tagDao.deleteTag(idTag);
    }

    @Override
    public Tag getTagById(Long idTag) {
        log.debug(String.format("Service: search tag by id %d", idTag));
        tagValidator.validateTagId(idTag);
        Optional<Tag> optionalTag = tagDao.getTagById(idTag);
        return optionalTag.orElseThrow(() -> new TagNotFoundException("message.wrong_tag_id"));
    }

    @Override
    public List<Tag> getTags(Integer pageNumber, Integer pageSize) {
        log.debug("Service: search all tags.");
        paginationValidator.validatePagination(pageNumber, pageSize);
        if (pageNumber != null && pageSize != null) {
            Integer offset = calculateOffset(pageNumber, pageSize);
            return tagDao.getTags(offset, pageSize);
        } else {
            return tagDao.getTags();
        }
    }

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

    @Override
    public Tag getTheMostUsedTag() {
        log.debug("Service: search the most used Tag");
        return tagDao.getTheMostUsedTag();
    }
}
