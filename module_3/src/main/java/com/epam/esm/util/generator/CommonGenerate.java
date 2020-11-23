package com.epam.esm.util.generator;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommonGenerate {
    private static final String PATH_SIMPLE_WORDS = "generate/simpleWords.txt";
    private static final String PATH_NAMES_WORDS = "generate/userNames.txt";
    private static final String PATH_SURNAMES_WORDS = "generate/userSurnames.txt";
    private static final String PATH_CERTIFICATE_NAMES = "generate/certificateNames.txt";
    private static final String PATH_CERTIFICATE_DESCRIPTIONS = "generate/certificateDescriptions.txt";
    private final static int TAG_COUNT = 1000;
    private final static int USER_COUNT = 1000;
    private final static int CERTIFICATE_COUNT = 1000;
    private final static int ORDER_COUNT = 1000;
    private final WordsReader wordsReader;
    private final TagGenerate tagGenerate;
    private final UserGenerate userGenerate;
    private final CertificateGenerate certificateGenerate;
    private final OrderGenerate orderGenerate;

    @Autowired
    public CommonGenerate(WordsReader wordsReader, TagGenerate tagGenerate, UserGenerate userGenerate, CertificateGenerate certificateGenerate, OrderGenerate orderGenerate) {
        this.wordsReader = wordsReader;
        this.tagGenerate = tagGenerate;
        this.userGenerate = userGenerate;
        this.certificateGenerate = certificateGenerate;
        this.orderGenerate = orderGenerate;
    }

    public void execute() {
        List<Tag> tags = tagGenerate.generateTags(TAG_COUNT, getWords(PATH_SIMPLE_WORDS));
        List<User> users = userGenerate.generateUsers(USER_COUNT, getWords(PATH_NAMES_WORDS), getWords(PATH_SURNAMES_WORDS));
        List<Certificate> certificates = certificateGenerate.generateCertificates(CERTIFICATE_COUNT,
                getWordsLowerCase(PATH_CERTIFICATE_NAMES), getWords(PATH_CERTIFICATE_DESCRIPTIONS), tags);
        orderGenerate.generateOrders(ORDER_COUNT, certificates, users);
    }

    private List<String> getWords(String path) {
        return wordsReader.readWordsFromFile(path);
    }

    private List<String> getWordsLowerCase(String path) {
        return wordsReader.readWordsLowerCaseFromFile(path);
    }
}
