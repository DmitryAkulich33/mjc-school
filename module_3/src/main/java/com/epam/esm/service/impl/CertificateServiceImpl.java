package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.domain.User;
import com.epam.esm.exceptions.CertificateNotFoundException;
import com.epam.esm.exceptions.UserNotFoundException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.util.CertificateValidator;
import com.epam.esm.util.OffsetCalculator;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
     * Offset's calculator for this service
     */
    private final OffsetCalculator offsetCalculator;

    /**
     * Logger for this service
     */
    private static Logger log = LogManager.getLogger(CertificateServiceImpl.class);

    private static final String UNDERSCORES = "_";
    private static final String ASC = "ASC";
    private static final String DESC = "DESC";

    /**
     * Constructor - creating a new object
     *
     * @param certificateDao       dao for this server
     * @param tagDao               dao for this server
     * @param certificateValidator validator for this service
     * @param tagService           service for this service
     * @param offsetCalculator     offset's calculator for this service
     */
    @Autowired
    public CertificateServiceImpl(CertificateDao certificateDao, TagDao tagDao, CertificateValidator certificateValidator,
                                  TagService tagService, OffsetCalculator offsetCalculator) {
        this.certificateDao = certificateDao;
        this.tagDao = tagDao;
        this.certificateValidator = certificateValidator;
        this.tagService = tagService;
        this.offsetCalculator = offsetCalculator;
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
        List<Tag> tagsForCertificate = tagService.updateTags(certificate.getTags());
        certificate.setTags(tagsForCertificate);

        return certificateDao.createCertificate(certificate);
    }

    /**
     * Update part of certificate
     *
     * @param certificate   certificate
     * @param idCertificate certificate
     * @return certificate
     */
    @Transactional
    @Override
    public Certificate updatePartCertificate(Certificate certificate, Long idCertificate) {
        log.debug(String.format("Service: update part certificate with id %d", idCertificate));
        certificateValidator.validateCertificateId(idCertificate);

        Certificate certificateToUpdate = getCertificateById(idCertificate);
        String name = composeCertificateName(certificate, certificateToUpdate);
        String description = composeCertificateDescription(certificate, certificateToUpdate);
        Double price = composeCertificatePrice(certificate, certificateToUpdate);
        Integer duration = composeCertificateDuration(certificate, certificateToUpdate);
        List<Tag> tags = composeCertificateTags(certificate, certificateToUpdate);

        certificateToUpdate.setName(name);
        certificateToUpdate.setDescription(description);
        certificateToUpdate.setPrice(price);
        certificateToUpdate.setDuration(duration);
        certificateToUpdate.setTags(tags);

        return certificateDao.updateCertificate(certificateToUpdate);
    }

    /**
     * Update whole certificate
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
        certificateValidator.validateCertificate(certificate);
        Certificate certificateFromDb = getCertificateById(idCertificate);
        List<Tag> tagsAfterUpdate = tagService.updateTags(certificate.getTags());

        certificate.setId(certificateFromDb.getId());
        certificate.setCreateDate(certificateFromDb.getCreateDate());
        certificate.setTags(tagsAfterUpdate);
        certificate.setLock(certificateFromDb.getLock());

        return certificateDao.updateCertificate(certificate);
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
                                             Certificate certificateToUpdate) {
        List<Tag> tags = certificateFromQuery.getTags();

        return tags == null ? certificateToUpdate.getTags() : tagService.updateTags(tags);
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
        getCertificateById(idCertificate);
        certificateDao.deleteCertificate(idCertificate);
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
        Optional<Certificate> certificate = certificateDao.getCertificateById(idCertificate);
        if(certificate.isPresent()){
            return certificate.get();
        } else {
            throw new CertificateNotFoundException("message.wrong_certificate_id");
        }
    }

    /**
     * Get certificates with params
     *
     * @param name       tag's name
     * @param search     a word or part of a word to search
     * @param sort       field and kind of sort
     * @param pageNumber page number
     * @param pageSize   page size
     * @return list of certificates
     */
    @Transactional(readOnly = true)
    @Override
    public List<Certificate> getCertificates(String name, String search, String sort, Integer pageNumber, Integer pageSize) {
        log.debug("Service: search certificates.");
        Integer offset = offsetCalculator.calculateOffset(pageNumber, pageSize);
        Boolean sortAsc = isSortAsc(sort);
        String sortField = getSortField(sort);

        return certificateDao.getCertificates(name, search, sortAsc, sortField, offset, pageSize);
    }

    /**
     * Get certificates by tags
     *
     * @param tagNames   list of tag's name
     * @param pageNumber page number
     * @param pageSize   page size
     * @return list of certificates
     */
    @Override
    public List<Certificate> getCertificatesByTags(List<String> tagNames, Integer pageNumber, Integer pageSize) {
        log.debug("Service: search certificates by tags' names.");
        Integer offset = offsetCalculator.calculateOffset(pageNumber, pageSize);
        return certificateDao.getCertificatesByTags(tagNames, offset, pageSize);
    }

    private Boolean isSortAsc(String sort) {
        if (isNotBlank(sort)) {
            String[] fields = sort.trim().split(UNDERSCORES);
            String sortType = fields[fields.length - 1].toUpperCase();
            switch (sortType) {
                case ASC:
                    return true;
                case DESC:
                    return false;
                default:
                    return null;
            }
        } else {
            return null;
        }
    }

    private String getSortField(String sort) {
        if (isNotBlank(sort)) {
            String[] fields = sort.trim().split(UNDERSCORES);
            String sortType = fields[fields.length - 1];
            return sort.trim().replace(UNDERSCORES + sortType.toLowerCase(), StringUtils.EMPTY);

        } else {
            return null;
        }
    }
}
