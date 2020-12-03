package com.epam.esm.view;

import com.epam.esm.domain.Tag;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CreateTagView {
    @Null
    @JsonView(Views.V1.class)
    private Long id;

    @NotBlank
    @Pattern(regexp = "^\\S.+$")
    @JsonView(Views.V1.class)
    private String name;

    public class Views {
        public class V1 {
        }
    }

    public static Tag createForm(CreateTagView tagView) {
        Tag tag = new Tag();
        tag.setName(tagView.getName());
        return tag;
    }

    public static Tag createForm(TagView tagView) {
        Tag tag = new Tag();
        tag.setName(tagView.getName());
        return tag;
    }

    public static List<Tag> createListForm(List<TagView> tagsView) {
        return tagsView.stream().map(CreateTagView::createForm).collect(Collectors.toList());
    }
}
