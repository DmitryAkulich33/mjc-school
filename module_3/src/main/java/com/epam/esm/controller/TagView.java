package com.epam.esm.controller;

import com.epam.esm.domain.Tag;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

@Data
public class TagView {

    @JsonView({TagView.Views.V1.class, CertificateView.Views.V1.class})
    private String name;

    public class Views {
        public class V1 {
        }
    }

    static TagView createFrom(Tag tag) {
        TagView view = new TagView();
        view.setName(tag.getName());
        return view;
    }
}
