package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Order;
import com.epam.esm.domain.Tag;
import com.epam.esm.domain.User;
import com.epam.esm.exceptions.TagNotFoundException;
import com.epam.esm.exceptions.WrongEnteredDataException;
import com.epam.esm.service.TagService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@Service
public class TagServiceImpl implements TagService {
    private final TagDao tagDao;
    private final UserDao userDao;

    private static Logger log = LogManager.getLogger(TagServiceImpl.class);

    @Autowired
    public TagServiceImpl(TagDao tagDao, UserDao userDao) {
        this.tagDao = tagDao;
        this.userDao = userDao;
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
        User user = userDao.getUserWithTheLargeSumOrders();
        List<Certificate> userCertificates = getCertificatesFromUserOrders(user.getOrders());
        List<Tag> userTags = getTagsFromUserCertificate(userCertificates);

        return userTags.stream()
                .collect(groupingBy(x -> x, counting()))
                .entrySet().stream()
                .max(comparingByValue())
                .get().getKey();
    }

    private List<Certificate> getCertificatesFromUserOrders (List<Order> orders){
        List<Certificate> certificates = new ArrayList<>();
        orders.forEach(s -> certificates.addAll(s.getCertificates()));
        return certificates;
    }

    private List<Tag> getTagsFromUserCertificate (List<Certificate> certificates){
        List<Tag> tags = new ArrayList<>();
        certificates.forEach(s -> tags.addAll(s.getTags()));
        return tags;
    }

    @Override
    public List<Tag> createTags(List<Tag> tags) {
        return tagDao.createTags(tags);
    }
}
