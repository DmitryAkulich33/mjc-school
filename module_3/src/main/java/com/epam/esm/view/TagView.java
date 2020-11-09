package com.epam.esm.view;

import com.epam.esm.domain.Tag;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class TagView {
    @JsonView({TagView.Views.V1.class, CertificateView.Views.V1.class})
    private Long id;

    @JsonView({TagView.Views.V1.class, CertificateView.Views.V1.class})
    private String name;

    public class Views {
        public class V1 {
        }
    }

    public static TagView createForm(Tag tag) {
        TagView view = new TagView();
        view.setId(tag.getId());
        view.setName(tag.getName());
        return view;
    }

    public static List<TagView> createListFrom(List<Tag> tags) {
        return tags.stream().map(TagView::createForm).collect(Collectors.toList());
    }
}
