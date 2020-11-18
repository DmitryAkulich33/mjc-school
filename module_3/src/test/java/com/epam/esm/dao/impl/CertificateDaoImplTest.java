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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = {DbConfig.class})
@Transactional
@SqlGroup({
        @Sql(scripts = "/drop_tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "/create_tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "/init_tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
})
class CertificateDaoImplTest {

    private static final Long CORRECT_ID_1 = 1L;
    private static final Long CORRECT_ID_2 = 2L;
    private static final Long CORRECT_ID_3 = 3L;
    private static final Long WRONG_ID = 100L;
    private static final String WRONG_CERTIFICATE_NAME = "super";
    private static final String CERTIFICATE_NAME_1 = "Certificate for one purchase";
    private static final String CERTIFICATE_NAME_2 = "Certificate for dinner in a restaurant";
    private static final Integer LOCK = 0;
    private static final Integer PAGE_SIZE_1 = 1;
    private static final Integer PAGE_SIZE_10 = 10;
    private static final Integer OFFSET = 0;
    private static final Integer WRONG_OFFSET = -2;
    private static final String CREATE_DATE = "createDate";
    private static final String TAG_NAME_1 = "food";
    private static final String TAG_NAME_2 = "delivery";
    private static final String TAG_NAME_3 = "spa";
    private static final String SEARCH = "shop";
    private static final String CREATION_DATE_2 = "2020-11-22T12:45:11";
    private static final String CREATION_DATE_3 = "2020-11-22T13:00:01";
    private static final String UPDATE_DATE = "2020-11-22T13:15:01";


    private Certificate certificate1;
    private Certificate certificate2;
    private Certificate certificate3;
    private Certificate certificateCreate;
    private Certificate certificateUpdate;
    private Certificate certificate4;

    private Tag tag1;
    private Tag tag2;
    private Tag newTag;

    private List<Tag> tags1;
    private List<Tag> tags2;
    private List<Tag> newTags;
    private List<String> tagNames1;
    private List<String> tagNames2;

    @Autowired
    private CertificateDao certificateDao;

    @BeforeEach
    public void setUp() {
        tag1 = new Tag();
        tag1.setId(CORRECT_ID_1);
        tag1.setName(TAG_NAME_1);
        tag1.setLock(LOCK);

        tag2 = new Tag();
        tag2.setId(CORRECT_ID_2);
        tag2.setName(TAG_NAME_2);
        tag2.setLock(LOCK);

        newTag = new Tag();
        newTag.setName(TAG_NAME_3);

        tags1 = new ArrayList<>(Arrays.asList(tag1, tag2));
        tags2 = new ArrayList<>(Collections.singletonList(tag1));
        newTags = new ArrayList<>(Arrays.asList(tag2, newTag));
        tagNames1 = new ArrayList<>(Arrays.asList(TAG_NAME_1, TAG_NAME_2));
        tagNames2 = new ArrayList<>(Collections.singletonList(TAG_NAME_2));

        certificate1 = new Certificate();
        certificate1.setId(CORRECT_ID_1);
        certificate1.setName(CERTIFICATE_NAME_1);
        certificate1.setDescription("Certificate for one going to the shop");
        certificate1.setPrice(50.0);
        certificate1.setCreateDate(LocalDateTime.parse("2020-10-22T11:45:11"));
        certificate1.setLock(LOCK);
        certificate1.setDuration(365);
        certificate1.setTags(tags1);

        certificate2 = new Certificate();
        certificate2.setId(CORRECT_ID_2);
        certificate2.setName(CERTIFICATE_NAME_2);
        certificate2.setDescription("Food and drink without check limit at Viet Express");
        certificate2.setPrice(100.0);
        certificate2.setCreateDate(LocalDateTime.parse(CREATION_DATE_2));
        certificate2.setLock(LOCK);
        certificate2.setDuration(100);
        certificate2.setTags(tags2);

        certificate3 = new Certificate();
        certificate3.setName("SPA certificate");
        certificate3.setDescription("Romantic SPA date for two any day");
        certificate3.setPrice(100.0);
        certificate3.setDuration(100);
        certificate3.setTags(newTags);

        certificate4 = new Certificate();
        certificate4.setId(CORRECT_ID_2);
        certificate4.setName("SPA certificate");
        certificate4.setDescription("Romantic SPA date for two any day");
        certificate4.setPrice(100.0);
        certificate4.setCreateDate(LocalDateTime.parse(CREATION_DATE_2));
        certificate4.setDuration(100);
        certificate4.setLock(LOCK);
        certificate4.setTags(tags1);

        certificateCreate = new Certificate();
        certificateCreate.setId(CORRECT_ID_3);
        certificateCreate.setName("SPA certificate");
        certificateCreate.setDescription("Romantic SPA date for two any day");
        certificateCreate.setPrice(100.0);
        certificateCreate.setCreateDate(LocalDateTime.parse(CREATION_DATE_3));
        certificateCreate.setLock(0);
        certificateCreate.setDuration(100);
        certificateCreate.setTags(newTags);
        certificateCreate.getTags().get(1).setId(CORRECT_ID_3);
        certificateCreate.getTags().get(1).setLock(LOCK);

        certificateUpdate = new Certificate();
        certificateUpdate.setId(CORRECT_ID_2);
        certificateUpdate.setName("SPA certificate");
        certificateUpdate.setDescription("Romantic SPA date for two any day");
        certificateUpdate.setPrice(100.0);
        certificateUpdate.setCreateDate(LocalDateTime.parse(CREATION_DATE_2));
        certificateUpdate.setLastUpdateDate(LocalDateTime.parse(UPDATE_DATE));
        certificateUpdate.setDuration(100);
        certificateUpdate.setLock(LOCK);
        certificateUpdate.setTags(tags1);

    }

    @Test
    public void testGetCertificateById() {
        Optional<Certificate> expected = Optional.ofNullable(certificate1);

        Optional<Certificate> actual = certificateDao.getCertificateById(CORRECT_ID_1);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificateById_WrongResult() {
        Optional<Certificate> expected = Optional.ofNullable(certificate2);

        Optional<Certificate> actual = certificateDao.getCertificateById(CORRECT_ID_1);

        Assertions.assertNotEquals(expected, actual);
    }

    @Test
    public void testGetCertificateById_NotFound() {
        Optional<Certificate> expected = Optional.empty();

        Optional<Certificate> actual = certificateDao.getCertificateById(WRONG_ID);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificateByName() {
        Optional<Certificate> expected = Optional.ofNullable(certificate1);

        Optional<Certificate> actual = certificateDao.getCertificateByName(CERTIFICATE_NAME_1);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificateByName_WrongResult() {
        Optional<Certificate> expected = Optional.ofNullable(certificate2);

        Optional<Certificate> actual = certificateDao.getCertificateByName(CERTIFICATE_NAME_1);

        Assertions.assertNotEquals(expected, actual);
    }

    @Test
    public void testGetCertificateByName_NotFound() {
        Optional<Certificate> expected = Optional.empty();

        Optional<Certificate> actual = certificateDao.getCertificateByName(WRONG_CERTIFICATE_NAME);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificates() {
        List<Certificate> expected = new ArrayList<>(Arrays.asList(certificate1, certificate2));

        List<Certificate> actual = certificateDao.getCertificates(null, null, null, null, OFFSET, PAGE_SIZE_10);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificates_Pagination() {
        List<Certificate> expected = new ArrayList<>(Collections.singletonList(certificate1));

        List<Certificate> actual = certificateDao.getCertificates(null, null, null, null, OFFSET, PAGE_SIZE_1);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificates_SortCreateDateDesc() {
        List<Certificate> expected = new ArrayList<>(Arrays.asList(certificate2, certificate1));

        List<Certificate> actual = certificateDao.getCertificates(null, null, false, CREATE_DATE, OFFSET, PAGE_SIZE_10);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificates_SortCreateDateDesc_WrongResult() {
        List<Certificate> expected = new ArrayList<>(Arrays.asList(certificate2, certificate1));

        List<Certificate> actual = certificateDao.getCertificates(null, null, true, CREATE_DATE, OFFSET, PAGE_SIZE_10);

        Assertions.assertNotEquals(expected, actual);
    }

    @Test
    public void testGetCertificates_SortCreateDateDesc_TagName() {
        List<Certificate> expected = new ArrayList<>(Collections.singletonList(certificate1));

        List<Certificate> actual = certificateDao.getCertificates(TAG_NAME_2, null, false, CREATE_DATE, OFFSET, PAGE_SIZE_10);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificates_SortCreateDateDesc_TagName_WrongResult() {
        List<Certificate> expected = new ArrayList<>(Collections.singletonList(certificate1));

        List<Certificate> actual = certificateDao.getCertificates(TAG_NAME_1, null, false, CREATE_DATE, OFFSET, PAGE_SIZE_10);

        Assertions.assertNotEquals(expected, actual);
    }

    @Test
    public void testGetCertificates_SortCreateDateDesc_TagName_Search() {
        List<Certificate> expected = new ArrayList<>(Collections.singletonList(certificate1));

        List<Certificate> actual = certificateDao.getCertificates(TAG_NAME_1, SEARCH, false, CREATE_DATE, OFFSET, PAGE_SIZE_10);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificates_SortCreateDateDesc_TagName_Search_WrongResult() {
        List<Certificate> expected = new ArrayList<>(Collections.singletonList(certificate2));

        List<Certificate> actual = certificateDao.getCertificates(TAG_NAME_1, SEARCH, false, CREATE_DATE, OFFSET, PAGE_SIZE_10);

        Assertions.assertNotEquals(expected, actual);
    }

    @Test
    public void testGetCertificates_Search_WrongResult() {
        List<Certificate> expected = new ArrayList<>(Collections.singletonList(certificate2));

        List<Certificate> actual = certificateDao.getCertificates(null, SEARCH, null, null, OFFSET, PAGE_SIZE_10);

        Assertions.assertNotEquals(expected, actual);
    }


    @Test
    public void testGetCertificates_CertificateDaoException() {
        assertThrows(CertificateDaoException.class, () -> {
            certificateDao.getCertificates(null, null, null, null, WRONG_OFFSET, PAGE_SIZE_10);
        });
    }

    @Test
    public void testGetCertificatesByTags_OneTagNames() {
        List<Certificate> expected = new ArrayList<>(Collections.singletonList(certificate1));

        List<Certificate> actual = certificateDao.getCertificatesByTags(tagNames2, OFFSET, PAGE_SIZE_10);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificatesByTags_OneTagNames_WrongResult() {
        List<Certificate> expected = new ArrayList<>(Collections.singletonList(certificate2));

        List<Certificate> actual = certificateDao.getCertificatesByTags(tagNames2, OFFSET, PAGE_SIZE_10);

        Assertions.assertNotEquals(expected, actual);
    }

    @Test
    public void testGetCertificatesByTags_TwoTagNames() {
        List<Certificate> expected = new ArrayList<>(Arrays.asList(certificate1, certificate2));

        List<Certificate> actual = certificateDao.getCertificatesByTags(tagNames1, OFFSET, PAGE_SIZE_10);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificatesByTags_CertificateDaoException() {
        assertThrows(CertificateDaoException.class, () -> {
            certificateDao.getCertificatesByTags(tagNames1, WRONG_OFFSET, PAGE_SIZE_10);
        });
    }

    @Test
    public void testCreateCertificate() {
        Certificate actual = certificateDao.createCertificate(certificate3);
        actual.setCreateDate(LocalDateTime.parse(CREATION_DATE_3));

        Assertions.assertEquals(certificateCreate, actual);
    }

    @Test
    public void testCreateCertificate_GetCertificateById() {
        certificateDao.createCertificate(certificate3);
        Certificate actual = certificateDao.getCertificateById(CORRECT_ID_3).get();
        actual.setCreateDate(LocalDateTime.parse(CREATION_DATE_3));

        Assertions.assertEquals(certificateCreate, actual);
    }

    @Test
    public void testCreateCertificate_WrongResult() {
        Optional<Certificate> expected = Optional.ofNullable(certificateCreate);

        Certificate createdCertificate = certificateDao.createCertificate(certificate3);

        Optional<Certificate> actual = Optional.ofNullable(createdCertificate);

        Assertions.assertNotEquals(expected, actual);
    }

    @Test
    public void testCreateCertificate_CertificateDuplicateException() {
        certificate3.setName(CERTIFICATE_NAME_1);

        assertThrows(CertificateDuplicateException.class, () -> {
            certificateDao.createCertificate(certificate3);
        });
    }

    @Test
    public void testDeleteCertificate() {
        List<Certificate> expected = new ArrayList<>(Collections.singletonList(certificate1));

        certificateDao.deleteCertificate(CORRECT_ID_2);
        List<Certificate> actual = certificateDao.getCertificates(null, null, null, null, OFFSET, PAGE_SIZE_10);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testDeleteCertificate_GetCertificateById() {
        Optional<Certificate> expected = Optional.empty();

        certificateDao.deleteCertificate(CORRECT_ID_2);
        Optional<Certificate> actual = certificateDao.getCertificateById(CORRECT_ID_2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testUpdateCertificate() {
        Optional<Certificate> expected = Optional.ofNullable(certificateUpdate);

        Certificate updatedCertificate = certificateDao.updateCertificate(certificate4);
        updatedCertificate.setLastUpdateDate(LocalDateTime.parse(UPDATE_DATE));

        Optional<Certificate> actual = Optional.ofNullable(updatedCertificate);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testUpdateCertificate_GetCertificateById() {
        Optional<Certificate> expected = Optional.ofNullable(certificateUpdate);

        certificateDao.updateCertificate(certificate4);

        Optional<Certificate> actual = certificateDao.getCertificateById(CORRECT_ID_2);
        actual.get().setLastUpdateDate(LocalDateTime.parse(UPDATE_DATE));

        Assertions.assertEquals(expected, actual);
    }

}
