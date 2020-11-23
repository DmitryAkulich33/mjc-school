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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Validated
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
    public ResponseEntity<TagView> getTagById(@PathVariable @NotNull @Positive Long id) {
        Tag tag = tagService.getTagById(id);
        TagView tagView = TagView.createForm(tag);

        tagView.add(linkTo(methodOn(TagController.class).getTagById(id)).withSelfRel());

        return new ResponseEntity<>(tagView, HttpStatus.OK);
    }

    @JsonView(TagView.Views.V1.class)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionModel<TagView>> getAllTags(@RequestParam(required = false) @Positive Integer pageNumber,
                                                               @RequestParam(required = false) @Positive Integer pageSize) {
        List<Tag> tags = tagService.getTags(pageNumber, pageSize);
        List<TagView> tagViews = TagView.createListForm(tags);

        Link link = linkTo(methodOn(TagController.class).getAllTags(pageNumber, pageSize)).withSelfRel();

        return new ResponseEntity<>(CollectionModel.of(tagViews, link), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Tag> deleteTag(@PathVariable @NotNull @Positive Long id) {
        tagService.deleteTag(id);

        return ResponseEntity.ok().build();
    }

    @JsonView(TagView.Views.V1.class)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagView> createTag(@Valid @RequestBody @JsonView(CreateTagView.Views.V1.class) CreateTagView tagView) {
        Tag tag = CreateTagView.createForm(tagView);
        Tag createdTag = tagService.createTag(tag);
        TagView view = TagView.createForm(createdTag);

        view.add(linkTo(methodOn(TagController.class).createTag(tagView)).withSelfRel());

        return new ResponseEntity<>(view, HttpStatus.CREATED);
    }

    @JsonView(TagView.Views.V1.class)
    @GetMapping(path = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagView> getTheMostUsedTag() {
        Tag tag = tagService.getTheMostUsedTag();
        TagView tagView = TagView.createForm(tag);

        tagView.add(linkTo(methodOn(TagController.class).getTheMostUsedTag()).withSelfRel());

        return new ResponseEntity<>(tagView, HttpStatus.OK);
    }
}
