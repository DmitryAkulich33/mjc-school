package com.epam.esm.dao.impl;

import com.epam.esm.config.DbConfig;
import com.epam.esm.dao.CertificateDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
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

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = {DbConfig.class})
@Transactional
//@SqlGroup({
//        @Sql(scripts = "/drop_tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
//        @Sql(scripts = "/create_tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
//        @Sql(scripts = "/init_tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//})
class CertificateDaoImplTest {

    private static final Long CORRECT_ID_1 = 1L;
    private static final Long CORRECT_ID_2 = 2L;
    private static final Long WRONG_ID = 100L;
    private static final String WRONG_SORT = "name";
    private static final String CORRECT_SORT = "createDate_asc";
    private static final String WRONG_NAME = "super";
    private static final String WRONG_CERTIFICATE_NAME = "super";
    private static final String CERTIFICATE_NAME_1 = "Certificate for one purchase";
    private static final String CERTIFICATE_NAME_2 = "Certificate for dinner in a restaurant";
    private static final Integer LOCK = 0;
    private static final Integer PAGE_SIZE_1 = 1;
    private static final Integer PAGE_SIZE_10 = 10;
    private static final Integer OFFSET = 0;

    private Certificate certificate1;
    private Certificate certificate2;
    private Certificate certificate3;
    private Certificate certificateUpdate;

    private Tag tag1;
    private Tag tag2;

    private List<Tag> tags1;
    private List<Tag> tags2;

    @Autowired
    private CertificateDao certificateDao;

    @BeforeEach
    public void setUp() {
        tag1 = new Tag();
        tag1.setId(CORRECT_ID_1);
        tag1.setName("food");
        tag1.setLock(LOCK);

        tag2 = new Tag();
        tag2.setId(CORRECT_ID_2);
        tag2.setName("delivery");
        tag2.setLock(LOCK);

        tags1 = new ArrayList<>(Arrays.asList(tag1, tag2));
        tags2 = new ArrayList<>(Collections.singletonList(tag1));

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
        certificate2.setCreateDate(LocalDateTime.parse("2020-11-22T12:45:11"));
        certificate2.setLock(LOCK);
        certificate2.setDuration(100);
        certificate2.setTags(tags2);

        certificate3 = new Certificate();
        certificate3.setName("SPA certificate");
        certificate3.setDescription("Romantic SPA date for two any day");
        certificate3.setPrice(100.0);
//        certificate3.setCreateDate(LocalDateTime.parse("2020-11-22T12:45:11"));
//        certificate3.setLastUpdateDate(LocalDateTime.parse("2020-12-02T10:15:33"));
        certificate3.setLock(0);
        certificate3.setDuration(100);

        certificateUpdate = new Certificate();
        certificateUpdate.setId(CORRECT_ID_2);
        certificateUpdate.setName("Certificate for dinner in a restaurant");
        certificateUpdate.setDescription("Food and drink without check limit at Viet Express");
        certificateUpdate.setPrice(150.0);
        certificateUpdate.setCreateDate(LocalDateTime.parse("2020-11-22T12:45:11"));
        certificateUpdate.setLock(0);
        certificateUpdate.setDuration(365);
    }

//    @Test
//    public void testGetTagById_WrongResult() {
//        Optional<Tag> expected = Optional.ofNullable(tag2);
//
//        Optional<Tag> actual = tagDao.getTagById(CORRECT_ID_1);
//
//        Assertions.assertNotEquals(expected, actual);
//    }

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

//    @Test
//    public void testCreateCertificate() {
//        Tag tag1 = new Tag();
//        tag1.setId(1L);
//        tag1.setName("food");
//        tag1.setLock(0);
//
//        Tag tag3 = new Tag();
//        tag3.setId(3L);
//        tag3.setName("new");
//        tag3.setLock(0);
//
//        List<Tag> tags = new ArrayList<>();
//        tags.add(tag1);
//        tags.add(tag3);
//
//
//        certificate3.setTags(tags);
//        Certificate actual = certificateDao.createCertificate(certificate3);
//        certificate3.setId(actual.getId());
//        certificate3.setCreateDate(actual.getCreateDate());
//        Certificate expected = certificateDao.getCertificateById(3L);
//
//        Assertions.assertEquals(certificate3, actual);
//        Assertions.assertEquals(certificate3, actual);
//
//    }
}