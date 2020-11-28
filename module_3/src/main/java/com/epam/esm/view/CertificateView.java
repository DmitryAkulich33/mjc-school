package com.epam.esm.view;

import com.epam.esm.domain.Certificate;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CertificateView extends RepresentationModel<CertificateView> {
    @JsonView({Views.V1.class, OrderView.Views.V1.class})
    private Long id;

    @JsonView({Views.V1.class, OrderView.Views.V1.class})
    private String name;

    @JsonView({Views.V1.class, OrderView.Views.V1.class})
    private String description;

    @JsonView({Views.V1.class, OrderView.Views.V1.class})
    private Double price;

    @JsonView({Views.V1.class, OrderView.Views.V1.class})
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @JsonView({Views.V1.class, OrderView.Views.V1.class})
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdateDate;

    @JsonView({Views.V1.class, OrderView.Views.V1.class})
    private Integer duration;

    @JsonView({Views.V1.class, OrderView.Views.V1.class})
    private List<TagView> tags;

    public class Views {
        public class V1 {
        }
    }

    public static CertificateView createForm(Certificate certificate) {
        CertificateView certificateView = new CertificateView();
        certificateView.setId(certificate.getId());
        certificateView.setName(certificate.getName());
        certificateView.setDescription(certificate.getDescription());
        certificateView.setPrice(certificate.getPrice());
        certificateView.setCreateDate(certificate.getCreateDate());
        certificateView.setLastUpdateDate(certificate.getLastUpdateDate());
        certificateView.setDuration(certificate.getDuration());
        certificateView.setTags(TagView.createListForm(certificate.getTags()));

        return certificateView;
    }

    public static List<CertificateView> createListForm(List<Certificate> certificates) {
        return certificates.stream().map(CertificateView::createForm).collect(Collectors.toList());
    }
}
