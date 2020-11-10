package com.epam.esm.view;

import com.epam.esm.domain.User;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserView {
    @JsonView(Views.V1.class)
    private Long id;

    @JsonView(Views.V1.class)
    private String name;

    @JsonView(Views.V1.class)
    private String surname;

    public class Views {
        public class V1 {
        }
    }

    public static UserView createForm(User user) {
        UserView userView = new UserView();
        userView.setId(user.getId());
        userView.setName(user.getName());
        userView.setSurname(user.getSurname());

        return userView;
    }

    public static List<UserView> createListForm(List<User> users) {

        return users.stream().map(UserView::createForm).collect(Collectors.toList());
    }
}
