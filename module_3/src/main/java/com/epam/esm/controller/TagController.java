package com.epam.esm.controller;

import com.epam.esm.domain.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.view.CreateTagView;
import com.epam.esm.view.TagView;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/v1/tags")
public class TagController {
    private final TagService tagService;
    private static final String PAGE_NUMBER_DEFAULT = "1";
    private static final String PAGE_SIZE_DEFAULT = "10";

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @JsonView(TagView.Views.V1.class)
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagView> getTagById(@PathVariable @NonNull Long id) {
        Tag tag = tagService.getTagById(id);
        TagView tagView = TagView.createForm(tag);

        tagView.add(linkTo(methodOn(TagController.class).getTagById(id)).withSelfRel());

        return new ResponseEntity<>(tagView, HttpStatus.OK);
    }

    @JsonView(TagView.Views.V1.class)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionModel<TagView>> getAllTags(@RequestParam(required = false, defaultValue = PAGE_NUMBER_DEFAULT) Integer pageNumber,
                                                               @RequestParam(required = false, defaultValue = PAGE_SIZE_DEFAULT) Integer pageSize) {
        List<Tag> tags = tagService.getTags(pageNumber, pageSize);
        List<TagView> tagViews = TagView.createListForm(tags);

        Link link = linkTo(methodOn(TagController.class).getAllTags(pageNumber, pageSize)).withSelfRel();

        return new ResponseEntity<>(CollectionModel.of(tagViews, link), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Tag> deleteTag(@PathVariable @NonNull Long id) {
        tagService.deleteTag(id);

        return ResponseEntity.ok().build();
    }

    @JsonView(TagView.Views.V1.class)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagView> createTag(@RequestBody @JsonView(CreateTagView.Views.V1.class) CreateTagView tagView) {
        Tag tag = CreateTagView.createForm(tagView);
        Tag createdTag = tagService.createTag(tag);
        TagView view = TagView.createForm(createdTag);

        view.add(linkTo(methodOn(TagController.class).createTag(tagView)).withSelfRel());

        return new ResponseEntity<>(view, HttpStatus.CREATED);
    }

    @JsonView(TagView.Views.V1.class)
    @GetMapping(path = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionModel<TagView>> getTheMostUsedTag() {
        List<Tag> tags = tagService.getTheMostUsedTag();
        List<TagView> tagViews = TagView.createListForm(tags);

        Link link = linkTo(methodOn(TagController.class).getTheMostUsedTag()).withSelfRel();

        return new ResponseEntity<>(CollectionModel.of(tagViews, link), HttpStatus.OK);
    }
}
