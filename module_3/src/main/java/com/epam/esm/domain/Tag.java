package com.epam.esm.domain;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Tag {
    private Long id;
    private String name;
    private Integer lock;

}
