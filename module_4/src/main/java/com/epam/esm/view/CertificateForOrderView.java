package com.epam.esm.view;

import com.epam.esm.domain.Certificate;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class CertificateForOrderView {
    @JsonView({Views.V1.class, OrderView.Views.V1.class})
    private Long id;

    public class Views {
        public class V1 {
        }
    }

    public static Certificate createForm(CertificateForOrderView certificateForOrderView) {
        Certificate certificate = new Certificate();
        certificate.setId(certificateForOrderView.getId());

        return certificate;
    }

    public static List<Certificate> createListForm(List<CertificateForOrderView> certificates) {
        return certificates.stream().map(CertificateForOrderView::createForm).collect(Collectors.toList());
    }
}
