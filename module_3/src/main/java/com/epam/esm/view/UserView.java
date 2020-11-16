package com.epam.esm.view;

import com.epam.esm.domain.User;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserView  extends RepresentationModel<UserView> {
    private String content;
    private static final String USERS = "users";

    @JsonCreator
    public UserView(@JsonProperty("content") String content) {
        this.content = content;
    }

    @JsonView({Views.V1.class, OrderView.Views.V1.class})
    private Long id;

    @JsonView({Views.V1.class, OrderView.Views.V1.class})
    private String name;

    @JsonView({Views.V1.class, OrderView.Views.V1.class})
    private String surname;

    public class Views {
        public class V1 {
        }
    }

    public static UserView createForm(User user) {
        UserView userView = new UserView(USERS);
        userView.setId(user.getId());
        userView.setName(user.getName());
        userView.setSurname(user.getSurname());

        return userView;
    }

    public static List<UserView> createListForm(List<User> users) {
        return users.stream().map(UserView::createForm).collect(Collectors.toList());
    }
}
