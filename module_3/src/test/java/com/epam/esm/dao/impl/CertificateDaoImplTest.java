package com.epam.esm.dao.impl;

import com.epam.esm.config.DbConfig;
import com.epam.esm.dao.CertificateDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.exceptions.CertificateDaoException;
import com.epam.esm.exceptions.CertificateDuplicateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ComponentScan(basePackages = {"com.epam.esm.dao", "com.epam.esm.domain"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CertificateDaoImplTest {

    private static final Long ID_1 = 1L;
    private static final Long ID_2 = 2L;
    private static final String TAG_NAME_1 = "food";
    private static final String TAG_NAME_2 = "delivery";
    private static final String CERTIFICATE_NAME_1 = "Certificate for one purchase";
    private static final String CERTIFICATE_NAME_2 = "Certificate for dinner in a restaurant";
    private static final String CERTIFICATE_DESCRIPTION_1 = "Certificate for one going to the shop";
    private static final String CERTIFICATE_DESCRIPTION_2 = "Food and drink without check limit at Viet Express";
    private static final Double CERTIFICATE_PRICE_1 = 50.0;
    private static final Double CERTIFICATE_PRICE_2 = 100.0;
    private static final String CERTIFICATE_DATE_1 = "2020-10-22T11:45:11";
    private static final String CERTIFICATE_DATE_2 = "2020-11-22T12:45:11";
    private static final Integer CERTIFICATE_DURATION_1 = 365;
    private static final Integer CERTIFICATE_DURATION_2 = 100;
    private static final Integer LOCK = 0;
    private static final Integer PAGE_SIZE_1 = 1;
    private static final Integer PAGE_SIZE_10 = 10;
    private static final Integer OFFSET_0 = 0;
    private static final Integer OFFSET_2 = 2;


    private Certificate certificate1;
    private Certificate certificate2;

    private Tag tag1;
    private Tag tag2;

    private List<Tag> tags1;
    private List<Tag> tags2;
    private List<Tag> newTags;
    private List<String> tagNames1;
    private List<String> tagNames2;

    @Autowired
    private CertificateDao certificateDao;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void setUp() {
        tag1 = new Tag();
        tag1.setName(TAG_NAME_1);
        tag1.setLock(LOCK);

        tag2 = new Tag();
        tag2.setName(TAG_NAME_2);
        tag2.setLock(LOCK);

        List<Tag> tags1 = new ArrayList<>(Arrays.asList(tag1, tag2));
        List<Tag> tags2 = new ArrayList<>(Collections.singletonList(tag1));

        certificate1 = new Certificate();
        certificate1.setName(CERTIFICATE_NAME_1);
        certificate1.setDescription(CERTIFICATE_DESCRIPTION_1);
        certificate1.setPrice(CERTIFICATE_PRICE_1);
        certificate1.setCreateDate(LocalDateTime.parse(CERTIFICATE_DATE_1));
        certificate1.setLock(LOCK);
        certificate1.setDuration(CERTIFICATE_DURATION_1);
        certificate1.setTags(tags1);

        certificate2 = new Certificate();
        certificate2.setName(CERTIFICATE_NAME_2);
        certificate2.setDescription(CERTIFICATE_DESCRIPTION_2);
        certificate2.setPrice(CERTIFICATE_PRICE_2);
        certificate2.setCreateDate(LocalDateTime.parse(CERTIFICATE_DATE_2));
        certificate2.setLock(LOCK);
        certificate2.setDuration(CERTIFICATE_DURATION_2);
        certificate2.setTags(tags2);
    }

    @Test
    public void testGetCertificateById() {
        Certificate expected = entityManager.persist(certificate1);

        Certificate actual = certificateDao.getCertificateById(expected.getId()).get();

        assertEquals(expected, actual);
        assert actual.getId() > 0;
        assert !actual.getTags().isEmpty();
    }

    @Test
    public void testGetCertificateById_NotFound() {
        Optional<Certificate> expected = Optional.empty();

        Optional<Certificate> actual = certificateDao.getCertificateById(ID_1);

        assertEquals(expected, actual);
    }
    
//    @Test
//    public void testGetCertificates() {
//        List<Certificate> expected = new ArrayList<>(Arrays.asList(certificate1, certificate2));
//
//        List<Certificate> actual = certificateDao.getCertificates(null, null, null, null, OFFSET, PAGE_SIZE_10);
//
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetCertificates_Pagination() {
//        List<Certificate> expected = new ArrayList<>(Collections.singletonList(certificate1));
//
//        List<Certificate> actual = certificateDao.getCertificates(null, null, null, null, OFFSET, PAGE_SIZE_1);
//
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetCertificates_SortCreateDateDesc() {
//        List<Certificate> expected = new ArrayList<>(Arrays.asList(certificate2, certificate1));
//
//        List<Certificate> actual = certificateDao.getCertificates(null, null, false, CREATE_DATE, OFFSET, PAGE_SIZE_10);
//
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetCertificates_SortCreateDateDesc_WrongResult() {
//        List<Certificate> expected = new ArrayList<>(Arrays.asList(certificate2, certificate1));
//
//        List<Certificate> actual = certificateDao.getCertificates(null, null, true, CREATE_DATE, OFFSET, PAGE_SIZE_10);
//
//        Assertions.assertNotEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetCertificates_SortCreateDateDesc_TagName() {
//        List<Certificate> expected = new ArrayList<>(Collections.singletonList(certificate1));
//
//        List<Certificate> actual = certificateDao.getCertificates(TAG_NAME_2, null, false, CREATE_DATE, OFFSET, PAGE_SIZE_10);
//
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetCertificates_SortCreateDateDesc_TagName_WrongResult() {
//        List<Certificate> expected = new ArrayList<>(Collections.singletonList(certificate1));
//
//        List<Certificate> actual = certificateDao.getCertificates(TAG_NAME_1, null, false, CREATE_DATE, OFFSET, PAGE_SIZE_10);
//
//        Assertions.assertNotEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetCertificates_SortCreateDateDesc_TagName_Search() {
//        List<Certificate> expected = new ArrayList<>(Collections.singletonList(certificate1));
//
//        List<Certificate> actual = certificateDao.getCertificates(TAG_NAME_1, SEARCH, false, CREATE_DATE, OFFSET, PAGE_SIZE_10);
//
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetCertificates_SortCreateDateDesc_TagName_Search_WrongResult() {
//        List<Certificate> expected = new ArrayList<>(Collections.singletonList(certificate2));
//
//        List<Certificate> actual = certificateDao.getCertificates(TAG_NAME_1, SEARCH, false, CREATE_DATE, OFFSET, PAGE_SIZE_10);
//
//        Assertions.assertNotEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetCertificates_Search_WrongResult() {
//        List<Certificate> expected = new ArrayList<>(Collections.singletonList(certificate2));
//
//        List<Certificate> actual = certificateDao.getCertificates(null, SEARCH, null, null, OFFSET, PAGE_SIZE_10);
//
//        Assertions.assertNotEquals(expected, actual);
//    }
//
//
//    @Test
//    public void testGetCertificates_CertificateDaoException() {
//        assertThrows(CertificateDaoException.class, () -> {
//            certificateDao.getCertificates(null, null, null, null, WRONG_OFFSET, PAGE_SIZE_10);
//        });
//    }
//
//    @Test
//    public void testGetCertificatesByTags_OneTagNames() {
//        List<Certificate> expected = new ArrayList<>(Collections.singletonList(certificate1));
//
//        List<Certificate> actual = certificateDao.getCertificatesByTags(tagNames2, OFFSET, PAGE_SIZE_10);
//
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetCertificatesByTags_OneTagNames_WrongResult() {
//        List<Certificate> expected = new ArrayList<>(Collections.singletonList(certificate2));
//
//        List<Certificate> actual = certificateDao.getCertificatesByTags(tagNames2, OFFSET, PAGE_SIZE_10);
//
//        Assertions.assertNotEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetCertificatesByTags_TwoTagNames() {
//        List<Certificate> expected = new ArrayList<>(Arrays.asList(certificate1, certificate2));
//
//        List<Certificate> actual = certificateDao.getCertificatesByTags(tagNames1, OFFSET, PAGE_SIZE_10);
//
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetCertificatesByTags_CertificateDaoException() {
//        assertThrows(CertificateDaoException.class, () -> {
//            certificateDao.getCertificatesByTags(tagNames1, WRONG_OFFSET, PAGE_SIZE_10);
//        });
//    }
//
//    @Test
//    public void testCreateCertificate() {
//        Certificate actual = certificateDao.createCertificate(certificate3);
////        actual.setCreateDate(LocalDateTime.parse(CREATION_DATE_3));
//        certificateCreate.setCreateDate(actual.getCreateDate());
//
//        Assertions.assertEquals(certificateCreate, actual);
//    }
//
//    @Test
//    public void testCreateCertificate_GetCertificateById() {
//        certificateDao.createCertificate(certificate3);
//        Certificate actual = certificateDao.getCertificateById(CORRECT_ID_3).get();
//        actual.setCreateDate(LocalDateTime.parse(CREATION_DATE_3));
//
//        Assertions.assertEquals(certificateCreate, actual);
//    }
//
//    @Test
//    public void testCreateCertificate_WrongResult() {
//        Optional<Certificate> expected = Optional.ofNullable(certificateCreate);
//
//        Certificate createdCertificate = certificateDao.createCertificate(certificate3);
//
//        Optional<Certificate> actual = Optional.ofNullable(createdCertificate);
//
//        Assertions.assertNotEquals(expected, actual);
//    }
//
//    @Test
//    public void testCreateCertificate_CertificateDuplicateException() {
//        certificate3.setName(CERTIFICATE_NAME_1);
//
//        assertThrows(CertificateDuplicateException.class, () -> {
//            certificateDao.createCertificate(certificate3);
//        });
//    }
//
//    @Test
//    public void testDeleteCertificate() {
//        List<Certificate> expected = new ArrayList<>(Collections.singletonList(certificate1));
//
//        certificateDao.deleteCertificate(CORRECT_ID_2);
//        List<Certificate> actual = certificateDao.getCertificates(null, null, null, null, OFFSET, PAGE_SIZE_10);
//
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testDeleteCertificate_GetCertificateById() {
//        Optional<Certificate> expected = Optional.empty();
//
//        certificateDao.deleteCertificate(CORRECT_ID_2);
//        Optional<Certificate> actual = certificateDao.getCertificateById(CORRECT_ID_2);
//
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testUpdateCertificate() {
////        Optional<Certificate> expected = Optional.of(certificateUpdate);
//
//        Certificate updatedCertificate = certificateDao.updateCertificate(certificate4);
//
////        certificateUpdate.setLastUpdateDate(updatedCertificate.getLastUpdateDate());
//
//
//
//        Assertions.assertEquals(certificateUpdate.getName(), updatedCertificate.getName());
//        Assertions.assertEquals(certificateUpdate.getId(), updatedCertificate.getId());
//        Assertions.assertEquals(certificateUpdate.getDescription(), updatedCertificate.getDescription());
//    }
//
//    @Test
//    public void testUpdateCertificate_GetCertificateById() {
//        Optional<Certificate> expected = Optional.ofNullable(certificateUpdate);
//
//        certificateDao.updateCertificate(certificate4);
//
//        Optional<Certificate> actual = certificateDao.getCertificateById(CORRECT_ID_2);
//        actual.get().setLastUpdateDate(LocalDateTime.parse(UPDATE_DATE));
//
//        Assertions.assertEquals(expected, actual);
//    }

}
