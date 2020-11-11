package com.epam.esm.view;

import com.epam.esm.domain.Certificate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateCertificateView {
    @JsonView(Views.V1.class)
    private Long id;

    @NonNull
    @JsonView(Views.V1.class)
    private String name;

    @NonNull
    @JsonView(Views.V1.class)
    private String description;

    @NonNull
    @JsonView(Views.V1.class)
    private Double price;

    @JsonView(Views.V1.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @JsonView(Views.V1.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdateDate;

    @NonNull
    @JsonView(Views.V1.class)
    private Integer duration;

    @NonNull
    @JsonView(Views.V1.class)
    private List<TagView> tags;

    public class Views {
        public class V1 {
        }
    }

    public static Certificate createForm(CreateCertificateView createCertificateView) {
        Certificate certificate = new Certificate();
        certificate.setId(createCertificateView.getId());
        certificate.setName(createCertificateView.getName());
        certificate.setDescription(createCertificateView.getDescription());
        certificate.setPrice(createCertificateView.getPrice());
        certificate.setCreateDate(createCertificateView.getCreateDate());
        certificate.setLastUpdateDate(createCertificateView.getLastUpdateDate());
        certificate.setDuration(createCertificateView.getDuration());
        certificate.setTags(CreateTagView.createListForm(createCertificateView.getTags()));

        return certificate;
    }
}
