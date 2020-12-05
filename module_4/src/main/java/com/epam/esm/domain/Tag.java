package com.epam.esm.domain;

import com.epam.esm.dao.audit.AuditTagListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@EntityListeners(AuditTagListener.class)
@ToString(exclude = "certificates")
@EqualsAndHashCode(exclude = "certificates")
@Entity(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tag", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name_tag", unique = true)
    private String name;

    @Column(name = "lock_tag")
    private Integer lock;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private List<Certificate> certificates;
}
