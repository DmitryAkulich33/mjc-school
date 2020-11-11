package com.epam.esm.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString(of = {"id", "name", "lock"})
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tag", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name_tag", unique = true, nullable = false)
    private String name;

    @Column(name = "lock_tag")
    private Integer lock;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tag_certificate",
            joinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id_tag"),
            inverseJoinColumns = @JoinColumn(name = "certificate_id", referencedColumnName = "id_certificate"))
    List<Certificate> certificates;
}
