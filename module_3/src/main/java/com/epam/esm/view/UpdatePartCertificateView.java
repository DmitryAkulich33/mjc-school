package com.epam.esm.view;

import com.epam.esm.domain.Certificate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.springframework.lang.NonNull;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UpdatePartCertificateView {
    @Null
    @JsonView(Views.V1.class)
    private Long id;

    @Pattern(regexp = "^([A-Z].+)$")
    @JsonView(Views.V1.class)
    private String name;

    @Pattern(regexp = "^([A-Z].+)$")
    @JsonView(Views.V1.class)
    private String description;

    @Positive
    @JsonView(Views.V1.class)
    private Double price;

    @JsonView(Views.V1.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @JsonView(Views.V1.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdateDate;

    @Positive
    @JsonView(Views.V1.class)
    private Integer duration;

    @JsonView(Views.V1.class)
    private List<TagView> tags;

    public class Views {
        public class V1 {
        }
    }

    public static Certificate createForm(UpdatePartCertificateView updatePartCertificateView) {
        Certificate certificate = new Certificate();
        certificate.setId(updatePartCertificateView.getId());
        certificate.setName(updatePartCertificateView.getName());
        certificate.setDescription(updatePartCertificateView.getDescription());
        certificate.setPrice(updatePartCertificateView.getPrice());
        certificate.setCreateDate(updatePartCertificateView.getCreateDate());
        certificate.setLastUpdateDate(updatePartCertificateView.getLastUpdateDate());
        certificate.setDuration(updatePartCertificateView.getDuration());
        List<TagView> tagsView = updatePartCertificateView.getTags();
        if (tagsView != null) {
            certificate.setTags(CreateTagView.createListForm(tagsView));
        } else {
            certificate.setTags(null);
        }
        return certificate;
    }
}
