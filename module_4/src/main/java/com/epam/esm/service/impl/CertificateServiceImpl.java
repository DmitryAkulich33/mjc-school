package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateRepository;
import com.epam.esm.dao.specification.CertificateSpecification;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.exceptions.CertificateDuplicateException;
import com.epam.esm.exceptions.CertificateNotFoundException;
import com.epam.esm.exceptions.WrongEnteredDataException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.TagService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class CertificateServiceImpl implements CertificateService {
    private final CertificateRepository certificateRepository;
    private final TagService tagService;
    private final CertificateSpecification certificateSpecification;

    private static Logger log = LogManager.getLogger(CertificateServiceImpl.class);

    private static final String UNDERSCORES = "_";
    private static final String ASC = "ASC";
    private static final String DESC = "DESC";

    @Autowired
    public CertificateServiceImpl(CertificateRepository certificateRepository, TagService tagService, CertificateSpecification certificateSpecification) {
        this.certificateRepository = certificateRepository;
        this.tagService = tagService;
        this.certificateSpecification = certificateSpecification;
    }

    @Transactional
    @Override
    public Certificate createCertificate(Certificate certificate) {
        log.debug("Service: creation certificate.");
        List<Tag> tagsForCertificate = tagService.updateTags(certificate.getTags());
        certificate.setTags(tagsForCertificate);
        try {
            return certificateRepository.save(certificate);
        } catch (DataIntegrityViolationException e) {
            throw new CertificateDuplicateException("message.certificate.exists", e);
        }
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

        return certificateRepository.save(certificateToUpdate);
    }

    @Transactional
    @Override
    public Certificate updateCertificate(Certificate certificate, Long idCertificate) {
        log.debug(String.format("Service: update certificate with id %d", idCertificate));
        Certificate certificateToUpdate = getCertificateById(idCertificate);

        List<Tag> tagsAfterUpdate = tagService.updateTags(certificate.getTags());

        certificateToUpdate.setName(certificate.getName());
        certificateToUpdate.setDescription(certificate.getDescription());
        certificateToUpdate.setDuration(certificate.getDuration());
        certificateToUpdate.setPrice(certificate.getPrice());
        certificateToUpdate.setTags(tagsAfterUpdate);

        return certificateRepository.save(certificateToUpdate);
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
        certificateRepository.deleteEntity(true, idCertificate);
    }

    @Override
    public Certificate getCertificateById(Long idCertificate) {
        log.debug(String.format("Service: search certificate by id %d", idCertificate));
        Optional<Certificate> optionalCertificate = certificateRepository.getEntityById(false, idCertificate);
        return optionalCertificate.orElseThrow(() -> new CertificateNotFoundException("message.wrong_certificate_id"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Certificate> getCertificates(String tagName, String searchField, String sortData, Integer pageNumber, Integer pageSize) {
        log.debug("Service: search certificates.");
        Specification<Certificate> specification = certificateSpecification.filter(searchField, tagName);
        Sort sort = getSort(sortData);
        if (pageNumber != null && pageSize != null) {
            checkPaginationGetCertificates(specification, pageNumber, pageSize);
            return certificateRepository.findAll(specification, PageRequest.of(pageNumber - 1, pageSize, sort)).getContent();
        } else if (pageNumber == null && pageSize == null) {
            return certificateRepository.findAll(specification, sort);
        } else {
            throw new WrongEnteredDataException("message.invalid_entered_data");
        }
    }

    private void checkPaginationGetCertificates(Specification<Certificate> specification, Integer pageNumber, Integer pageSize) {
        long countFromDb = certificateRepository.count(specification);
        long countFromRequest = (pageNumber - 1) * pageSize;
        if (countFromDb <= countFromRequest) {
            throw new WrongEnteredDataException("message.invalid_entered_data");
        }
    }

    private Sort getSort(String sortData) {
        return sortData == null ? Sort.unsorted() : selectSort(sortData);
    }

    private Sort selectSort(String sortData) {
        String sortField = getSortField(sortData);
        return isSortAsc(sortData) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Certificate> getCertificatesByTags(List<String> tagNames, Integer pageNumber, Integer pageSize) {
        log.debug("Service: search certificates by tags' names.");
        Specification<Certificate> specification = certificateSpecification.tagNames(tagNames);
        if (pageNumber != null && pageSize != null) {
            checkPaginationGetCertificatesByTags(specification, pageNumber, pageSize);
            return certificateRepository.findAll(specification, PageRequest.of(pageNumber - 1, pageSize)).getContent();
        } else if (pageNumber == null && pageSize == null) {
            return certificateRepository.findAll(specification);
        } else {
            throw new WrongEnteredDataException("message.invalid_entered_data");
        }
    }

    private void checkPaginationGetCertificatesByTags(Specification<Certificate> specification, Integer pageNumber, Integer pageSize) {
        long countFromDb = certificateRepository.count(specification);
        long countFromRequest = (pageNumber - 1) * pageSize;
        if (countFromDb <= countFromRequest) {
            throw new WrongEnteredDataException("message.invalid_entered_data");
        }
    }

    @Transactional
    @Override
    public List<Certificate> createCertificates(List<Certificate> certificates) {
        try {
            return (List<Certificate>) certificateRepository.saveAll(certificates);
        } catch (DataIntegrityViolationException e) {
            throw new CertificateDuplicateException("message.certificate.exists", e);
        }
    }

    private Boolean isSortAsc(String sort) {
        String[] fields = sort.trim().split(UNDERSCORES);
        String sortType = fields[fields.length - 1].toUpperCase();
        switch (sortType) {
            case ASC:
                return true;
            case DESC:
                return false;
            default:
                throw new WrongEnteredDataException("message.invalid_entered_data");
        }
    }

    private String getSortField(String sort) {
        if (isNotBlank(sort)) {
            String[] fields = sort.trim().split(UNDERSCORES);
            String sortType = fields[fields.length - 1];
            return sort.trim().replace(UNDERSCORES + sortType.toLowerCase(), StringUtils.EMPTY);
        } else {
            throw new WrongEnteredDataException("message.invalid_entered_data");
        }
    }
}
