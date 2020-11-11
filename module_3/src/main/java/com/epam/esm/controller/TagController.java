package com.epam.esm.controller;

import com.epam.esm.domain.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.view.CreateTagView;
import com.epam.esm.view.TagView;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/tags")
public class TagController {
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @JsonView(TagView.Views.V1.class)
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagView> getTagById(@PathVariable Long id) {
        Tag tag = tagService.getTagById(id);
        TagView tagView = TagView.createForm(tag);

        return new ResponseEntity<>(tagView, HttpStatus.OK);
    }

    @JsonView(TagView.Views.V1.class)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TagView>> getAllTags() {
        List<Tag> tags = tagService.getTags();
        List<TagView> tagViews = TagView.createListForm(tags);

        return new ResponseEntity<>(tagViews, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Tag> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);

        return ResponseEntity.ok().build();
    }

    @JsonView(TagView.Views.V1.class)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagView> createTag(@RequestBody @JsonView(CreateTagView.Views.V1.class) CreateTagView tagView) {
        Tag tag = CreateTagView.createForm(tagView);
        Tag createdTag = tagService.createTag(tag);
        TagView view = TagView.createForm(createdTag);

        return new ResponseEntity<>(view, HttpStatus.CREATED);
    }
}
