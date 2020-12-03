package com.epam.esm.util.generator;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@PropertySource("classpath:generate/generate.properties")
public class CommonGenerate {
    private static final String PATH_SIMPLE_WORDS = "generate/simpleWords.txt";
    private static final String PATH_NAMES_WORDS = "generate/userNames.txt";
    private static final String PATH_SURNAMES_WORDS = "generate/userSurnames.txt";
    private static final String PATH_CERTIFICATE_NAMES = "generate/certificateNames.txt";
    private static final String PATH_CERTIFICATE_DESCRIPTIONS = "generate/certificateDescriptions.txt";

    private final WordsReader wordsReader;
    private final TagGenerate tagGenerate;
    private final UserGenerate userGenerate;
    private final CertificateGenerate certificateGenerate;
    private final OrderGenerate orderGenerate;
    private final Environment environment;

    private List<Tag> tags;
    private List<User> users;
    private List<Certificate> certificates;

    @Autowired
    public CommonGenerate(WordsReader wordsReader, TagGenerate tagGenerate, UserGenerate userGenerate, CertificateGenerate certificateGenerate, OrderGenerate orderGenerate, Environment environment) {
        this.wordsReader = wordsReader;
        this.tagGenerate = tagGenerate;
        this.userGenerate = userGenerate;
        this.certificateGenerate = certificateGenerate;
        this.orderGenerate = orderGenerate;
        this.environment = environment;
    }

    public void execute() {
        boolean isGenerate = environment.getRequiredProperty("isGenerate", Boolean.class);
        if (isGenerate) {
            int tagAmount = environment.getRequiredProperty("tag.amount", Integer.class);
            int userAmount = environment.getRequiredProperty("user.amount", Integer.class);
            int certificateAmount = environment.getRequiredProperty("certificate.amount", Integer.class);
            int orderAmount = environment.getRequiredProperty("order.amount", Integer.class);

            tags = tagGenerate.generateTags(tagAmount, getWords(PATH_SIMPLE_WORDS));
            users = userGenerate.generateUsers(userAmount, getWords(PATH_NAMES_WORDS), getWords(PATH_SURNAMES_WORDS));
            certificates = certificateGenerate.generateCertificates(certificateAmount,
                    getWordsLowerCase(PATH_CERTIFICATE_NAMES), getWords(PATH_CERTIFICATE_DESCRIPTIONS), tags);
            orderGenerate.generateOrders(orderAmount, certificates, users);
        }
    }

    private List<String> getWords(String path) {
        return wordsReader.readWordsFromFile(path);
    }

    private List<String> getWordsLowerCase(String path) {
        return wordsReader.readWordsLowerCaseFromFile(path);
    }
}
