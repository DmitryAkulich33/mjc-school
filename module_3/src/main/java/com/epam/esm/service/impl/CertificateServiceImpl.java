package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.exceptions.CertificateNotFoundException;
import com.epam.esm.exceptions.WrongEnteredDataException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.TagService;
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
    private final CertificateDao certificateDao;
    private final TagService tagService;

    private static Logger log = LogManager.getLogger(CertificateServiceImpl.class);

    private static final String UNDERSCORES = "_";
    private static final String ASC = "ASC";
    private static final String DESC = "DESC";

    @Autowired
    public CertificateServiceImpl(CertificateDao certificateDao, TagService tagService) {
        this.certificateDao = certificateDao;
        this.tagService = tagService;
    }

    @Transactional
    @Override
    public Certificate createCertificate(Certificate certificate) {
        log.debug("Service: creation certificate.");
        List<Tag> tagsForCertificate = tagService.updateTags(certificate.getTags());
        certificate.setTags(tagsForCertificate);

        return certificateDao.createCertificate(certificate);
    }

    @Transactional
    @Override
    public Certificate updatePartCertificate(Certificate certificate, Long idCertificate) {
        log.debug(String.format("Service: update part certificate with id %d", idCertificate));
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

    @Transactional
    @Override
    public Certificate updateCertificate(Certificate certificate, Long idCertificate) {
        log.debug(String.format("Service: update certificate with id %d", idCertificate));
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

    @Transactional
    @Override
    public void deleteCertificate(Long idCertificate) {
        log.debug(String.format("Service: deletion certificate with id %d", idCertificate));
        getCertificateById(idCertificate);
        certificateDao.deleteCertificate(idCertificate);
    }

    @Transactional(readOnly = true)
    @Override
    public Certificate getCertificateById(Long idCertificate) {
        log.debug(String.format("Service: search certificate by id %d", idCertificate));
        Optional<Certificate> optionalCertificate = certificateDao.getCertificateById(idCertificate);
        return optionalCertificate.orElseThrow(() -> new CertificateNotFoundException("message.wrong_certificate_id"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Certificate> getCertificates(String name, String search, String sort, Integer pageNumber, Integer pageSize) {
        log.debug("Service: search certificates.");
        Boolean sortAsc = isSortAsc(sort);
        String sortField = getSortField(sort);
        if (pageNumber != null && pageSize != null) {
            Integer offset = calculateOffset(pageNumber, pageSize);
            return certificateDao.getCertificates(name, search, sortAsc, sortField, offset, pageSize);
        } else if (pageNumber == null && pageSize == null) {
            return certificateDao.getCertificates(name, search, sortAsc, sortField);
        } else {
            throw new WrongEnteredDataException("message.invalid_entered_data");
        }
    }

    @Override
    public List<Certificate> getCertificatesByTags(List<String> tagNames, Integer pageNumber, Integer pageSize) {
        log.debug("Service: search certificates by tags' names.");
        if (pageNumber != null && pageSize != null) {
            Integer offset = calculateOffset(pageNumber, pageSize);
            return certificateDao.getCertificatesByTags(tagNames, offset, pageSize);
        } else if (pageNumber == null && pageSize == null) {
            return certificateDao.getCertificatesByTags(tagNames);
        } else {
            throw new WrongEnteredDataException("message.invalid_entered_data");
        }
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
