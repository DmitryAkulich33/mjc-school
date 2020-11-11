package com.epam.esm.view;

import com.epam.esm.domain.Certificate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UpdateCertificateView {
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

    public static Certificate createForm(UpdateCertificateView updateCertificateView) {
        Certificate certificate = new Certificate();
        certificate.setId(updateCertificateView.getId());
        certificate.setName(updateCertificateView.getName());
        certificate.setDescription(updateCertificateView.getDescription());
        certificate.setPrice(updateCertificateView.getPrice());
        certificate.setCreateDate(updateCertificateView.getCreateDate());
        certificate.setLastUpdateDate(updateCertificateView.getLastUpdateDate());
        certificate.setDuration(updateCertificateView.getDuration());
        List<TagView> tagsView = updateCertificateView.getTags();
        if (tagsView != null) {
            certificate.setTags(CreateTagView.createListForm(tagsView));
        } else {
            certificate.setTags(null);
        }
        return certificate;
    }
}
