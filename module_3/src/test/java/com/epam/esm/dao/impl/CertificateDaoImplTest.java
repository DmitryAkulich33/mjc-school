package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.exceptions.CertificateDaoException;
import com.epam.esm.exceptions.CertificateDuplicateException;
import com.epam.esm.exceptions.WrongEnteredDataException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ComponentScan(basePackages = {"com.epam.esm.dao", "com.epam.esm.domain"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CertificateDaoImplTest {

    private static final Long ID_1 = 1L;
    private static final Long ID_2 = 2L;
    private static final String TAG_NAME_1 = "food";
    private static final String TAG_NAME_2 = "delivery";
    private static final String WRONG_TAG_NAME_3 = "spa";
    private static final String CERTIFICATE_NAME_1 = "Certificate for one purchase";
    private static final String CERTIFICATE_NAME_2 = "Certificate for dinner in a restaurant";
    private static final String CERTIFICATE_DESCRIPTION_1 = "Certificate for one going to the shop";
    private static final String CERTIFICATE_DESCRIPTION_2 = "Food and drink without check limit at Viet Express";
    private static final Double CERTIFICATE_PRICE_1 = 50.0;
    private static final Double CERTIFICATE_PRICE_2 = 100.0;
    private static final Integer CERTIFICATE_DURATION_1 = 365;
    private static final Integer CERTIFICATE_DURATION_2 = 100;
    private static final Integer LOCK = 0;
    private static final Integer PAGE_SIZE_1 = 1;
    private static final Integer PAGE_SIZE_2 = 2;
    private static final Integer OFFSET_0 = 0;
    private static final Integer OFFSET_2 = 2;
    private static final Integer WRONG_OFFSET = -2;
    private static final String SORT_CREATE_DATE = "createDate";
    private static final String SORT_NAME = "name";
    private static final String SEARCH_1 = "shop";
    private static final String SEARCH_2 = "Certificate";

    private Certificate certificate1;
    private Certificate certificate2;
    private Certificate createCertificate;
    private Certificate updateCertificate;

    private List<String> tagNames;
    private List<String> wrongTagNames;

    @Autowired
    private CertificateDao certificateDao;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void setUp() {
        Tag tag1 = new Tag();
        tag1.setName(TAG_NAME_1);

        Tag tag2 = new Tag();
        tag2.setName(TAG_NAME_2);

        entityManager.persist(tag1);
        entityManager.persist(tag2);

        List<Tag> tags1 = new ArrayList<>(Arrays.asList(tag1, tag2));
        List<Tag> tags2 = new ArrayList<>(Collections.singletonList(tag1));

        certificate1 = new Certificate();
        certificate1.setName(CERTIFICATE_NAME_1);
        certificate1.setDescription(CERTIFICATE_DESCRIPTION_1);
        certificate1.setPrice(CERTIFICATE_PRICE_1);
        certificate1.setDuration(CERTIFICATE_DURATION_1);
        certificate1.setTags(tags1);

        certificate2 = new Certificate();
        certificate2.setName(CERTIFICATE_NAME_2);
        certificate2.setDescription(CERTIFICATE_DESCRIPTION_2);
        certificate2.setPrice(CERTIFICATE_PRICE_2);
        certificate2.setDuration(CERTIFICATE_DURATION_2);
        certificate2.setTags(tags2);

        createCertificate = new Certificate();
        createCertificate.setName(CERTIFICATE_NAME_1);
        createCertificate.setDescription(CERTIFICATE_DESCRIPTION_1);
        createCertificate.setPrice(CERTIFICATE_PRICE_1);
        createCertificate.setDuration(CERTIFICATE_DURATION_1);
        createCertificate.setTags(tags1);

        updateCertificate = new Certificate();
        updateCertificate.setId(ID_1);
        updateCertificate.setName(CERTIFICATE_NAME_1);
        updateCertificate.setDescription(CERTIFICATE_DESCRIPTION_1);
        updateCertificate.setPrice(CERTIFICATE_PRICE_2);
        updateCertificate.setDuration(CERTIFICATE_DURATION_2);
        updateCertificate.setTags(tags2);

        tagNames = Arrays.asList(TAG_NAME_1, TAG_NAME_2);
        wrongTagNames = Arrays.asList(TAG_NAME_1, TAG_NAME_2, WRONG_TAG_NAME_3);
    }

    @Test
    public void testGetCertificateById() {
        Certificate expected = entityManager.persist(certificate1);

        Certificate actual = certificateDao.getCertificateById(expected.getId()).get();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificateById_NotFound() {
        Optional<Certificate> expected = Optional.empty();

        Optional<Certificate> actual = certificateDao.getCertificateById(ID_1);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificates_WithoutParams() {
        Certificate expected1 = entityManager.persist(certificate1);
        Certificate expected2 = entityManager.persist(certificate2);
        List<Certificate> expected = Arrays.asList(expected1, expected2);

        List<Certificate> actual = certificateDao.getCertificates(null, null, null, null);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificates_WithoutParams_Pagination() {
        Certificate expected1 = entityManager.persist(certificate1);
        entityManager.persist(certificate2);
        List<Certificate> expected = Collections.singletonList(expected1);

        List<Certificate> actual = certificateDao.getCertificates(null, null, null, null, OFFSET_0, PAGE_SIZE_1);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificates_WrongEnteredDataException() {
        entityManager.persist(certificate1);
        entityManager.persist(certificate2);
        assertThrows(WrongEnteredDataException.class, () -> {
            certificateDao.getCertificates(null, null, null, null, OFFSET_2, PAGE_SIZE_2);
        });
    }

    @Test
    public void testGetCertificates_SortCreateDateAsc() throws InterruptedException {
        Certificate expected1 = entityManager.persist(certificate1);
        Thread.sleep(100);
        Certificate expected2 = entityManager.persist(certificate2);
        List<Certificate> expected = Arrays.asList(expected1, expected2);

        List<Certificate> actual = certificateDao.getCertificates(null, null, true, SORT_CREATE_DATE, OFFSET_0, PAGE_SIZE_2);

        Assertions.assertEquals(expected, actual);

    }

    @Test
    public void testGetCertificates_SortCreateDateDesc() throws InterruptedException {
        Certificate expected1 = entityManager.persist(certificate1);
        Thread.sleep(100);
        Certificate expected2 = entityManager.persist(certificate2);
        List<Certificate> expected = new ArrayList<>(Arrays.asList(expected2, expected1));

        List<Certificate> actual = certificateDao.getCertificates(null, null, false, SORT_CREATE_DATE, OFFSET_0, PAGE_SIZE_2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificates_SortNameAsc() {
        Certificate expected1 = entityManager.persist(certificate1);
        Certificate expected2 = entityManager.persist(certificate2);
        List<Certificate> expected = new ArrayList<>(Arrays.asList(expected2, expected1));

        List<Certificate> actual = certificateDao.getCertificates(null, null, true, SORT_NAME, OFFSET_0, PAGE_SIZE_2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificates_SortCreateNameDesc() {
        Certificate expected1 = entityManager.persist(certificate1);
        Certificate expected2 = entityManager.persist(certificate2);
        List<Certificate> expected = Arrays.asList(expected1, expected2);

        List<Certificate> actual = certificateDao.getCertificates(null, null, false, SORT_NAME, OFFSET_0, PAGE_SIZE_2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificates_SortCreateDateDesc_TagName() {
        Certificate expected1 = entityManager.persist(certificate1);
        entityManager.persist(certificate2);
        List<Certificate> expected = new ArrayList<>(Collections.singletonList(expected1));

        List<Certificate> actual = certificateDao.getCertificates(TAG_NAME_2, null, false, SORT_CREATE_DATE, OFFSET_0, PAGE_SIZE_2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificates_TagName() {
        Certificate expected1 = entityManager.persist(certificate1);
        Certificate expected2 = entityManager.persist(certificate2);
        List<Certificate> expected = new ArrayList<>(Arrays.asList(expected1, expected2));

        List<Certificate> actual = certificateDao.getCertificates(TAG_NAME_1, null, null, null, OFFSET_0, PAGE_SIZE_2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificates_TagName_EmptyResult() {
        entityManager.persist(certificate1);
        entityManager.persist(certificate2);

        List<Certificate> actual = certificateDao.getCertificates(WRONG_TAG_NAME_3, null, null, null, OFFSET_0, PAGE_SIZE_2);

        assert actual.isEmpty();
    }

    @Test
    public void testGetCertificates_SortCreateDateDesc_TagName_Search() {
        Certificate expected1 = entityManager.persist(certificate1);
        entityManager.persist(certificate2);
        List<Certificate> expected = new ArrayList<>(Collections.singletonList(expected1));

        List<Certificate> actual = certificateDao.getCertificates(TAG_NAME_2, SEARCH_1, false, SORT_CREATE_DATE, OFFSET_0, PAGE_SIZE_2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificates_Search_ResultOneCertificate() {
        Certificate expected1 = entityManager.persist(certificate1);
        entityManager.persist(certificate2);
        List<Certificate> expected = new ArrayList<>(Collections.singletonList(expected1));

        List<Certificate> actual = certificateDao.getCertificates(null, SEARCH_1, null, null, OFFSET_0, PAGE_SIZE_2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificates_Search_ResultListCertificate() {
        Certificate expected1 = entityManager.persist(certificate1);
        Certificate expected2 = entityManager.persist(certificate2);
        List<Certificate> expected = new ArrayList<>(Arrays.asList(expected1, expected2));

        List<Certificate> actual = certificateDao.getCertificates(null, SEARCH_2, null, null, OFFSET_0, PAGE_SIZE_2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificates_TagName_Search() {
        Certificate expected1 = entityManager.persist(certificate1);
        entityManager.persist(certificate2);
        List<Certificate> expected = new ArrayList<>(Collections.singletonList(expected1));

        List<Certificate> actual = certificateDao.getCertificates(TAG_NAME_2, SEARCH_1, null, null, OFFSET_0, PAGE_SIZE_2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificates_CertificateDaoException() {
        assertThrows(CertificateDaoException.class, () -> {
            certificateDao.getCertificates(null, null, null, null, WRONG_OFFSET, PAGE_SIZE_2);
        });
    }

    @Test
    public void testGetCertificatesByTags() {
        Certificate expected1 = entityManager.persist(certificate1);
        entityManager.persist(certificate2);
        List<Certificate> expected = new ArrayList<>(Collections.singletonList(expected1));

        List<Certificate> actual = certificateDao.getCertificatesByTags(tagNames, OFFSET_0, PAGE_SIZE_2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetCertificatesByTags_EmptyResult() {
        entityManager.persist(certificate1);
        entityManager.persist(certificate2);

        List<Certificate> actual = certificateDao.getCertificatesByTags(wrongTagNames, OFFSET_0, PAGE_SIZE_2);

        assert actual.isEmpty();
    }

    @Test
    public void testGetCertificatesByTags_CertificateDaoException() {
        assertThrows(CertificateDaoException.class, () -> {
            certificateDao.getCertificatesByTags(tagNames, WRONG_OFFSET, PAGE_SIZE_2);
        });
    }

    @Test
    public void testCreateCertificate() {
        LocalDateTime currentDate = LocalDateTime.now();

        Certificate actual = certificateDao.createCertificate(createCertificate);

        assert actual.getId() > 0;
        assert actual.getName().equals(CERTIFICATE_NAME_1);
        assert actual.getDescription().equals(CERTIFICATE_DESCRIPTION_1);
        assert actual.getPrice().equals(CERTIFICATE_PRICE_1);
        assert actual.getDuration().equals(CERTIFICATE_DURATION_1);
        assert actual.getLock().equals(LOCK);
        assert actual.getCreateDate().isAfter(currentDate.minusSeconds(2));
        assert actual.getLastUpdateDate() == null;
        assert actual.getTags().equals(createCertificate.getTags());
    }

    @Test
    public void testCreateCertificate_CertificateDuplicateException() {
        entityManager.persist(certificate1);
        assertThrows(CertificateDuplicateException.class, () -> {
            certificateDao.createCertificate(createCertificate);
        });
    }

    @Test
    public void testCreateCertificate_CertificateDaoException() {
        createCertificate.setId(ID_1);
        assertThrows(CertificateDuplicateException.class, () -> {
            certificateDao.createCertificate(createCertificate);
        });
    }

    @Test
    public void testDeleteCertificate_GetCertificates() {
        entityManager.persist(certificate1);

        certificateDao.deleteCertificate(certificate1.getId());

        TypedQuery<Certificate> query = entityManager.getEntityManager().createQuery(
                "SELECT c FROM certificate c WHERE c.id=?1 AND c.lock=0", Certificate.class);
        Certificate actual = query.setParameter(1, certificate1.getId()).getResultStream().findAny().orElse(null);

        assertNull(actual);
    }

    @Test
    public void testUpdateCertificate() {
        Certificate expected = entityManager.persist(certificate1);
        System.out.println(expected);
        System.out.println(updateCertificate);

        Certificate actual = certificateDao.updateCertificate(updateCertificate);

        assert actual.getName().equals(expected.getName());
        assert actual.getId().equals(expected.getId());
        assert actual.getDescription().equals(expected.getDescription());
        assert actual.getDuration().equals(CERTIFICATE_DURATION_2);
        assert actual.getPrice().equals(CERTIFICATE_PRICE_2);
        assert actual.getTags().get(0).equals(updateCertificate.getTags().get(0));
    }

    @Test
    public void testUpdateCertificate_CertificateDaoException() {
        entityManager.persist(certificate1);
        updateCertificate.setId(ID_2);

        assertThrows(CertificateDaoException.class, () -> {
            certificateDao.updateCertificate(updateCertificate);
        });
    }
}
