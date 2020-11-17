package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.util.OffsetCalculator;
import com.epam.esm.util.TagValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
        return tagDao.getTagById(idTag);
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
     * @param tags          list of tags
     * @return list of tags
     */
    @Transactional
    @Override
    public List<Tag> updateTags(List<Tag> tags) {
        log.debug("Service: update tags in certificate");
        List<Tag> tagsToUpdate = new ArrayList<>();

//        List<Tag> tagsFromDB = tagDao.getTags();
        Set<Tag> uniqueTags = new HashSet<>(tags);
        for (Tag tag : uniqueTags) {
            String nameTag = tag.getName();
            if(tagDao.getTagByName(nameTag) != null){
                Tag currentTag = tagDao.getTagByName(nameTag);
                tagsToUpdate.add(currentTag);
            } else {
                Tag tagToCreate = new Tag();
                tagToCreate.setName(nameTag);
                Tag createdTag = tagDao.createTag(tagToCreate);
                tagsToUpdate.add(createdTag);
            }
//            if (isNewTag(tagsFromDB, nameTag)) {
//                Tag tagToCreate = new Tag();
//                tagToCreate.setName(nameTag);
//                Tag createdTag = tagDao.createTag(tagToCreate);
//                tagsToUpdate.add(createdTag);
//            } else {
//                Tag currentTag = tagDao.getTagByName(nameTag);
//                tagsToUpdate.add(currentTag);
//            }
        }
        return tagsToUpdate;
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
}
