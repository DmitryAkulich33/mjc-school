package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.exceptions.CertificateDaoException;
import com.epam.esm.exceptions.TagDaoException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.util.CertificateValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CertificateServiceImpl implements CertificateService {
    /**
     * Dao for this server
     */
    private final CertificateDao certificateDao;

    /**
     * Dao for this server
     */
    private final TagDao tagDao;

    /**
     * Validator for this service
     */
    private final CertificateValidator certificateValidator;

    /**
     * Service for this service
     */
    private final TagService tagService;

    /**
     * Logger for this service
     */
    private static Logger log = LogManager.getLogger(CertificateServiceImpl.class);

    /**
     * Constructor - creating a new object
     *
     * @param certificateDao       dao for this server
     * @param tagDao               dao for this server
     * @param certificateValidator validator for this service
     * @param tagService           service for this service
     */
    @Autowired
    public CertificateServiceImpl(CertificateDao certificateDao,
                                  TagDao tagDao,
                                  CertificateValidator certificateValidator,
                                  TagService tagService) {
        this.certificateDao = certificateDao;
        this.tagDao = tagDao;
        this.certificateValidator = certificateValidator;
        this.tagService = tagService;
    }

    /**
     * Create certificate
     *
     * @param certificate certificate
     * @return certificate
     */
    @Transactional
    @Override
    public Certificate createCertificate(Certificate certificate) {
        log.debug("Service: creation certificate.");
        certificateValidator.validateCertificate(certificate);
//        Certificate createdCertificate = certificateDao.createCertificate(certificate);
//        List<Tag> tagsForCertificate = new ArrayList<>();
//        Long id = createdCertificate.getId();
//        List<Tag> tagsFromDB = tagDao.getAllTags();
//        Set<Tag> uniqueTags = new HashSet<>(certificate.getTags());
//
//        for (Tag tag : uniqueTags) {
//            String nameTag = tag.getName();
//            if (tagService.isNewTag(tagsFromDB, nameTag)) {
//                Tag tagToCreate = new Tag();
//                tagToCreate.setName(nameTag);
//                Tag createdTag = tagDao.createTag(tagToCreate);
//                tagsForCertificate.add(createdTag);
//                Long idTag = createdTag.getId();
//                tagDao.createTagCertificate(idTag, id);
//            } else {
//                Tag currentTag = tagDao.getTagByName(nameTag);
//                Long idTag = currentTag.getId();
//                tagDao.createTagCertificate(idTag, id);
//                tagsForCertificate.add(currentTag);
//            }
//        }
//
//        createdCertificate.setTags(tagsForCertificate);
//        return createdCertificate;
        return certificateDao.createCertificate(certificate);
    }

    /**
     * Update certificate
     *
     * @param certificate   certificate
     * @param idCertificate certificate
     * @return certificate
     */
    @Transactional
    @Override
    public Certificate updateCertificate(Certificate certificate, Long idCertificate) {
        log.debug(String.format("Service: update certificate with id %d", idCertificate));
        certificateValidator.validateCertificateId(idCertificate);

        Certificate certificateToUpdate = certificateDao.getCertificateById(idCertificate);
        String name = composeCertificateName(certificate, certificateToUpdate);
        String description = composeCertificateDescription(certificate, certificateToUpdate);
        Double price = composeCertificatePrice(certificate, certificateToUpdate);
        Integer duration = composeCertificateDuration(certificate, certificateToUpdate);
        List<Tag> tags = composeCertificateTags(certificate, certificateToUpdate, idCertificate);

        certificateToUpdate.setName(name);
        certificateToUpdate.setDescription(description);
        certificateToUpdate.setPrice(price);
        certificateToUpdate.setDuration(duration);
        certificateToUpdate.setTags(tags);

        return certificateDao.updateCertificate(certificateToUpdate);
    }

    private String composeCertificateName(Certificate certificateFromQuery, Certificate certificateToUpdate) {
        String name = certificateFromQuery.getName();
        return name == null ? certificateToUpdate.getName() : name;
    }

    private String composeCertificateDescription(Certificate certificateFromQuery, Certificate certificateToUpdate) {
        String description = certificateFromQuery.getDescription();
        return description == null ? certificateToUpdate.getDescription() : description;
    }

    private Double composeCertificatePrice(Certificate certificateFromQuery, Certificate certificateToUpdate) {
        Double price = certificateFromQuery.getPrice();
        return price == null ? certificateToUpdate.getPrice() : price;
    }

    private Integer composeCertificateDuration(Certificate certificateFromQuery, Certificate certificateToUpdate) {
        Integer duration = certificateFromQuery.getDuration();
        return duration == null ? certificateToUpdate.getDuration() : duration;
    }

    private List<Tag> composeCertificateTags(Certificate certificateFromQuery,
                                             Certificate certificateToUpdate,
                                             Long idCertificate) {
        List<Tag> tags = certificateFromQuery.getTags();
        List<Tag> tagsAfterUpdate = null;
        if (tags != null) {
            tagService.updateTags(tags, idCertificate);
            tagsAfterUpdate = tagDao.getCertificateTags(idCertificate);
        }
        return tags == null ? certificateToUpdate.getTags() : tagsAfterUpdate;
    }

    /**
     * Delete certificate by id
     *
     * @param idCertificate certificate's id
     */
    @Transactional
    @Override
    public void deleteCertificate(Long idCertificate) {
        log.debug(String.format("Service: deletion certificate with id %d", idCertificate));
        certificateValidator.validateCertificateId(idCertificate);
        certificateDao.getCertificateById(idCertificate);
        int result = certificateDao.deleteCertificate(idCertificate);
        if(result == 0){
            throw new CertificateDaoException("message.dao.exception");
        }
    }

    /**
     * Get certificate by id
     *
     * @param idCertificate certificate's id
     * @return certificate
     */
    @Transactional(readOnly = true)
    @Override
    public Certificate getCertificateById(Long idCertificate) {
        log.debug(String.format("Service: search certificate by id %d", idCertificate));
        certificateValidator.validateCertificateId(idCertificate);
        Certificate certificate = certificateDao.getCertificateById(idCertificate);
//        List<Tag> tags = tagDao.getCertificateTags(idCertificate);
//        certificate.setTags(tags);
        return certificate;
    }

    /**
     * Get certificates with params
     *
     * @param name   tag's name
     * @param search a word or part of a word to search
     * @param sort   field and kind of sort
     * @return list of certificates
     */
    @Transactional(readOnly = true)
    @Override
    public List<Certificate> getCertificates(String name, String search, String sort) {
        log.debug("Service: search certificates.");
        List<Certificate> certificates = certificateDao.getCertificates(name, search, sort);
//        certificates.forEach(s -> s.setTags(tagDao.getCertificateTags(s.getId())));
        return certificates;
    }
}
