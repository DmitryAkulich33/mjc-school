package com.epam.esm.controller;

import com.epam.esm.domain.Certificate;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CertificateView {
    @JsonView(Views.V1.class)
    private Long id;
    @JsonView(Views.V1.class)
    private String name;
    @JsonView(Views.V1.class)
    private String description;
    @JsonView(Views.V1.class)
    private Double price;
    @JsonView(Views.V1.class)
    private LocalDateTime createDate;
    @JsonView(Views.V1.class)
    private LocalDateTime lastUpdateDate;
    @JsonView(Views.V1.class)
    private Integer duration;
    @JsonView(Views.V1.class)
    private List<TagView> tags;

    public class Views {
        public class V1 {
        }
    }

    static CertificateView createFrom(Certificate certificate) {
        CertificateView view = new CertificateView();
        view.setId(certificate.getId());
        view.setName(certificate.getName());
        view.setDescription(certificate.getDescription());
        view.setPrice(certificate.getPrice());
        view.setCreateDate(certificate.getCreateDate());
        view.setLastUpdateDate(certificate.getLastUpdateDate());
        view.setDuration(certificate.getDuration());
        view.setTags(certificate.getTags().stream().map(TagView::createFrom).collect(Collectors.toList()));
        return view;
    }
}
