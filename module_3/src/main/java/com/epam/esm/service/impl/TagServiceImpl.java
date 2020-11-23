package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Tag;
import com.epam.esm.exceptions.TagNotFoundException;
import com.epam.esm.exceptions.WrongEnteredDataException;
import com.epam.esm.service.TagService;
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

    private static Logger log = LogManager.getLogger(TagServiceImpl.class);

    @Autowired
    public TagServiceImpl(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Transactional
    @Override
    public Tag createTag(Tag tag) {
        log.debug("Service: creation tag.");
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
        Optional<Tag> optionalTag = tagDao.getTagById(idTag);
        return optionalTag.orElseThrow(() -> new TagNotFoundException("message.wrong_tag_id"));
    }

    @Override
    public List<Tag> getTags(Integer pageNumber, Integer pageSize) {
        log.debug("Service: search all tags.");
        if (pageNumber != null && pageSize != null) {
            Integer offset = calculateOffset(pageNumber, pageSize);
            return tagDao.getTags(offset, pageSize);
        } else if (pageNumber == null && pageSize == null) {
            return tagDao.getTags();
        } else {
            throw new WrongEnteredDataException("message.invalid_entered_data");
        }
    }

    @Transactional
    @Override
    public List<Tag> updateTags(List<Tag> tags) {
        log.debug("Service: update tags in certificate");
        Set<Tag> uniqueTags = new HashSet<>(tags);

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

    @Override
    public List<Tag> createTags(List<Tag> tags) {
        return tagDao.createTags(tags);
    }
}
