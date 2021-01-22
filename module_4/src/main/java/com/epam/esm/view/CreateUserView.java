package com.epam.esm.view;

import com.epam.esm.domain.User;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateUserView {
    @NotBlank()
    @JsonView(Views.V1.class)
    private String login;

    @NotBlank()
    @JsonView(Views.V1.class)
    private String password;

    @NotBlank()
    @JsonView(Views.V1.class)
    private String name;

    @NotBlank()
    @JsonView(Views.V1.class)
    private String surname;

    public class Views {
        public class V1 {
        }
    }

    public static User createForm(CreateUserView createUserView) {
        User user = new User();
        user.setLogin(createUserView.getLogin());
        user.setPassword(createUserView.getPassword());
        user.setName(createUserView.getName());
        user.setSurname(createUserView.getSurname());

        return user;
    }
}
