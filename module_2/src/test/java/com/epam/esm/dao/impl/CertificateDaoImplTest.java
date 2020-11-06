package com.epam.esm.dao.impl;

import com.epam.esm.config.TestConfig;
import com.epam.esm.dao.CertificateDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.exceptions.CertificateNotFoundException;
import com.epam.esm.exceptions.CertificateDaoException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@SqlGroup({
        @Sql(scripts = "/drop_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
        @Sql(scripts = "/create_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
        @Sql(scripts = "/init_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
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
        certificate3.setCreateDate(LocalDateTime.parse("2020-11-22T12:45:11"));
        certificate3.setLastUpdateDate(LocalDateTime.parse("2020-12-02T10:15:33"));
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

    @Test
    public void testGetCertificateById_AnotherId() {
        Certificate actual = certificateDao.getCertificateById(CORRECT_ID_2);

        Assertions.assertNotEquals(certificate1, actual);
    }

    @Test
    public void testGetCertificateById_CertificateNotFoundException() {
        assertThrows(CertificateNotFoundException.class, () -> {
            certificateDao.getCertificateById(WRONG_ID);
        });
    }

    @Test
    public void testGetCertificates() {
        List<Certificate> expected = new ArrayList<>(Arrays.asList(certificate1, certificate2));

        List<Certificate> actual = certificateDao.getCertificates(null, null, CORRECT_SORT);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificates_CertificateDaoException() {
        assertThrows(CertificateDaoException.class, () -> {
            certificateDao.getCertificates(null, null, WRONG_SORT);
        });
    }

    @Test
    public void testDeleteCertificate() {
        List<Certificate> expected = new ArrayList<>(Collections.singletonList(certificate2));

        certificateDao.deleteCertificate(CORRECT_ID_1);

        List<Certificate> actual = certificateDao.getCertificates(null, null, null);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testUpdateCertificate() {
        Certificate actual = certificateDao.updateCertificate(certificateUpdate);

        Assertions.assertEquals(certificateUpdate, actual);
    }

    @Test
    public void testCreateCertificate() {
        Certificate actual = certificateDao.createCertificate(certificate3);

        Assertions.assertEquals(certificate3, actual);
    }
}