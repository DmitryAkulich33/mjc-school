package com.epam.esm.service.impl;

import com.epam.esm.dao.TagRepository;
import com.epam.esm.dao.UserRepository;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Order;
import com.epam.esm.domain.Tag;
import com.epam.esm.domain.User;
import com.epam.esm.exceptions.TagDuplicateException;
import com.epam.esm.exceptions.TagNotFoundException;
import com.epam.esm.exceptions.WrongEnteredDataException;
import com.epam.esm.service.TagService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    private static Logger log = LogManager.getLogger(TagServiceImpl.class);

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, UserRepository userRepository) {
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public Tag createTag(Tag tag) {
        log.debug("Service: creation tag.");
        try {
            return tagRepository.save(tag);
        } catch (DataIntegrityViolationException e) {
            throw new TagDuplicateException("message.tag.exists", e);
        }
    }

    @Transactional
    @Override
    public void deleteTag(Long idTag) {
        log.debug(String.format("Service: deletion tag by id  %d", idTag));
        getTagById(idTag);
        tagRepository.deleteEntity(true, idTag);
    }

    @Override
    public Tag getTagById(Long idTag) {
        log.debug(String.format("Service: search tag by id %d", idTag));
        Optional<Tag> optionalTag = tagRepository.getEntityById(false, idTag);
        return optionalTag.orElseThrow(() -> new TagNotFoundException("message.wrong_tag_id"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Tag> getTags(Integer pageNumber, Integer pageSize) {
        log.debug("Service: search all tags.");
        if (pageNumber != null && pageSize != null) {
            checkPagination(pageNumber, pageSize);
            return tagRepository.getEntities(false, PageRequest.of(pageNumber - 1, pageSize));
        } else if (pageNumber == null && pageSize == null) {
            return tagRepository.getEntities(false);
        } else {
            throw new WrongEnteredDataException("message.invalid_entered_data");
        }
    }

    private void checkPagination(Integer pageNumber, Integer pageSize) {
        long countFromDb = tagRepository.count();
        long countFromRequest = (pageNumber - 1) * pageSize;
        if (countFromDb <= countFromRequest) {
            throw new WrongEnteredDataException("message.invalid_entered_data");
        }
    }

    @Transactional
    @Override
    public List<Tag> updateTags(List<Tag> tags) {
        log.debug("Service: update tags in certificate");
        Set<Tag> uniqueTags = new HashSet<>(tags);

        List<Tag> tagsToCreate = uniqueTags.stream()
                .filter(tag -> !tagRepository.findByName(tag.getName()).isPresent())
                .map(tagRepository::save)
                .collect(Collectors.toList());

        List<Tag> existingTags = uniqueTags.stream()
                .filter(tag -> !tagsToCreate.contains(tag))
                .map(tag -> tagRepository.findByName(tag.getName()).get())
                .collect(Collectors.toList());

        existingTags.addAll(tagsToCreate);
        return existingTags;
    }

    @Override
    public Tag getTheMostUsedTag() {
        log.debug("Service: search the most used Tag");
        User user = userRepository.getUserWithTheLargeSumOrders().stream().findFirst().get();
        List<Certificate> userCertificates = getCertificatesFromUserOrders(user.getOrders());
        List<Tag> userTags = getTagsFromUserCertificate(userCertificates);

        return userTags.stream()
                .collect(groupingBy(x -> x, counting()))
                .entrySet().stream()
                .max(comparingByValue())
                .get().getKey();
    }

    private List<Certificate> getCertificatesFromUserOrders(List<Order> orders) {
        List<Certificate> certificates = new ArrayList<>();
        orders.forEach(s -> certificates.addAll(s.getCertificates()));
        return certificates;
    }

    private List<Tag> getTagsFromUserCertificate(List<Certificate> certificates) {
        List<Tag> tags = new ArrayList<>();
        certificates.forEach(s -> tags.addAll(s.getTags()));
        return tags;
    }

    @Transactional
    @Override
    public List<Tag> createTags(List<Tag> tags) {
        try {
            return (List<Tag>) tagRepository.saveAll(tags);
        } catch (DataIntegrityViolationException e) {
            throw new TagDuplicateException("message.tag.exists", e);
        }
    }
}
