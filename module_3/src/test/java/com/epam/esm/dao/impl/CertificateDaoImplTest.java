package com.epam.esm.dao.impl;

import com.epam.esm.config.DbConfig;
import com.epam.esm.dao.CertificateDao;
import com.epam.esm.domain.Certificate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = {DbConfig.class})
//@SqlGroup({
//        @Sql(scripts = "/drop_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
//        @Sql(scripts = "/create_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
//        @Sql(scripts = "/init_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//})
class CertificateDaoImplTest {

    private static final Long CORRECT_ID_1 = 1L;
    private static final Long CORRECT_ID_2 = 2L;
    private static final Long WRONG_ID = 100L;
    private static final String WRONG_SORT = "name";
    private static final String CORRECT_SORT = "creation_date_asc";

    private Certificate certificate1;
    private Certificate certificate2;
    private Certificate certificate3;
    private Certificate certificateUpdate;

    @Autowired
    private CertificateDao certificateDao;

    @BeforeEach
    public void setUp() {
        certificate1 = new Certificate();
        certificate1.setId(CORRECT_ID_1);
        certificate1.setName("Certificate for one purchase");
        certificate1.setDescription("Certificate for one going to the shop");
        certificate1.setPrice(50.0);
        certificate1.setCreateDate(LocalDateTime.parse("2020-10-22T11:45:11"));
        certificate1.setLock(0);
        certificate1.setDuration(365);

        certificate2 = new Certificate();
        certificate2.setId(CORRECT_ID_2);
        certificate2.setName("Certificate for dinner in a restaurant");
        certificate2.setDescription("Food and drink without check limit at Viet Express");
        certificate2.setPrice(100.0);
        certificate2.setCreateDate(LocalDateTime.parse("2020-11-22T12:45:11"));
        certificate2.setLock(0);
        certificate2.setDuration(100);

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

    @Test
    public void testGetCertificateById() {
        Certificate actual = certificateDao.getCertificateById(CORRECT_ID_1);

        Assertions.assertEquals(certificate1, actual);
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