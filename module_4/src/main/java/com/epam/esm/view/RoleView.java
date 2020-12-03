package com.epam.esm.view;

import com.epam.esm.domain.Role;
import com.epam.esm.domain.Tag;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RoleView extends RepresentationModel<RoleView> {

    @JsonView({TagView.Views.V1.class,
            CertificateView.Views.V1.class,
            CreateCertificateView.Views.V1.class,
            UpdateCertificateView.Views.V1.class,
            UpdatePartCertificateView.Views.V1.class,
            OrderView.Views.V1.class,
            UserView.Views.V1.class})
    private String name;

    public class Views {
        public class V1 {
        }
    }

    public static RoleView createForm(Role role) {
        RoleView view = new RoleView();
        view.setName(role.getName());
        return view;
    }
}
