package com.epam.esm.util.generator;

import com.epam.esm.exceptions.OpenFileException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class WordsReader {

    public List<String> readWordsFromFile(String path) {
        Resource xlsRes = new ClassPathResource(path);
        List<String> wordsFromFile = null;
        try (Stream<String> stream = Files.lines(Paths.get(xlsRes.getURI()))) {
            wordsFromFile = stream.distinct().collect(Collectors.toList());
        } catch (IOException e) {
            throw new OpenFileException("message.error_open_file");
        }

        return wordsFromFile;
    }

    public List<String> readWordsLowerCaseFromFile(String path) {
        Resource xlsRes = new ClassPathResource(path);
        List<String> wordsFromFile;
        try (Stream<String> stream = Files.lines(Paths.get(xlsRes.getURI()))) {
            wordsFromFile = stream
                    .map(String::toLowerCase)
                    .distinct()
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new OpenFileException("message.error_open_file");
        }

        return wordsFromFile;
    }
}
