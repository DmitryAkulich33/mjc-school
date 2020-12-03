package com.epam.esm.view;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequestView extends RepresentationModel<AuthenticationRequestView> {
    @NotBlank()
    @JsonView(Views.V1.class)
    private String login;

    @NotBlank()
    @JsonView(Views.V1.class)
    private String password;

    public class Views {
        public class V1 {
        }
    }
}
