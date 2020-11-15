package com.epam.esm.domain;

import com.epam.esm.dao.audit.AuditTagListener;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@EntityListeners(AuditTagListener.class)
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
    @Column(name = "id_tag")
    private Long id;

    @Column(name = "name_tag")
    private String name;

    @Column(name = "lock_tag")
    private Integer lock;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    List<Certificate> certificates;
}
