package com.epam.esm.util.generator;

import com.epam.esm.domain.Tag;
import com.epam.esm.service.TagService;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class TagGenerate {
    private final TagService tagService;

    @Autowired
    public TagGenerate(TagService tagService) {
        this.tagService = tagService;
    }

    private List<String> getWordsToCreateTag(int countTags, List<String> allWords) {
        List<String> tagsName = new ArrayList<>();
        IntStream.range(0, countTags).map(i -> RandomUtils.nextInt(1, allWords.size())).forEach(random -> {
            tagsName.add(allWords.get(random - 1));
            allWords.remove(random - 1);
        });
        return tagsName;
    }

    public List<Tag> generateTags(int countTags, List<String> allWords) {
        List<String> tagNames = getWordsToCreateTag(countTags, allWords);
        List<Tag> tags = new ArrayList<>();
        tagNames.forEach(word -> {
            Tag tag = new Tag();
            tag.setName(word);
            tags.add(tag);
        });
        return tagService.createTags(tags);
    }
}
