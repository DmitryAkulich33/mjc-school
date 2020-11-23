package com.epam.esm.util.generator;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.service.CertificateService;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CertificateGenerate {
    private static final int MIN_DURATION = 90;
    private static final int MAX_DURATION = 365;
    private static final int MIN_PRICE = 10;
    private static final int MAX_PRICE = 300;
    private static final int MIN_TAG_COUNT = 1;
    private static final int MAX_TAG_COUNT = 3;
    private static final int MIN_DESCRIPTION = 1;
    private static final int MAX_DESCRIPTION = 575;
    private final CertificateService certificateService;

    @Autowired
    public CertificateGenerate(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    private List<String> getWordsToCreate(int countCertificates, List<String> allWords) {
        List<String> certificateNames = new ArrayList<>();
        for (int i = 0; i < countCertificates; i++) {
            int random = RandomUtils.nextInt(1, allWords.size());
            certificateNames.add(allWords.get(random - 1));
            allWords.remove(random - 1);
        }
        return certificateNames;
    }

    public List<Certificate> generateCertificates(int countCertificates, List<String> names, List<String> descriptions,
                                                  List<Tag> tags) {
        List<String> certificateNames = getWordsToCreate(countCertificates, names);
        List<String> certificateDescriptions = getWordsToCreate(MAX_DESCRIPTION, descriptions);
        List<Certificate> certificates = new ArrayList<>();
        for (int i = 0; i < countCertificates; i++) {
            Certificate certificate = new Certificate();
            certificate.setName(String.format("Certificate of %s", certificateNames.get(i)));

            int randomDescription = RandomUtils.nextInt(MIN_DESCRIPTION, MAX_DESCRIPTION);
            certificate.setDescription(certificateDescriptions.get(randomDescription));

            double randomPrice = Math.ceil(RandomUtils.nextDouble(MIN_PRICE, MAX_PRICE));
            certificate.setPrice(randomPrice);

            int randomDuration = RandomUtils.nextInt(MIN_DURATION, MAX_DURATION);
            certificate.setDuration(randomDuration);

            List<Tag> certificateTags = new ArrayList<>();
            int randomTagCount = RandomUtils.nextInt(MIN_TAG_COUNT, MAX_TAG_COUNT);
            int tagsSize = tags.size();
            int randomTag = RandomUtils.nextInt(0, tagsSize - randomTagCount - 1);
            for (int j = 0; j < randomTagCount; j++) {
                Tag tag = tags.get(randomTag);
                randomTag++;
                certificateTags.add(tag);
            }
            certificate.setTags(certificateTags);

            certificates.add(certificate);
        }
        return certificateService.createCertificates(certificates);
    }
}
