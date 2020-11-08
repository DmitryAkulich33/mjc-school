package com.epam.esm.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tag")
@NamedQueries({
        @NamedQuery(name = Tag.QueryNames.LOCK_BY_ID,
                query = "UPDATE tag SET lock_tag=1 WHERE id_tag=:idTag"),
        @NamedQuery(name = Tag.QueryNames.FIND_ALL,
                query = "SELECT t FROM tag t WHERE lock_tag=0"),
        @NamedQuery(name = Tag.QueryNames.FIND_BY_ID,
                query = "SELECT t FROM tag t WHERE lock_tag=0 AND id_tag=:idTag"),
        @NamedQuery(name = Tag.QueryNames.FIND_BY_NAME,
                query = "SELECT t FROM tag t WHERE lock_tag=0 AND name_tag=:nameTag")
})
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_tag", nullable = false)
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

    public static final class QueryNames {
        public static final String FIND_BY_ID = "Tag.getById";
        public static final String FIND_BY_NAME = "Tag.getByName";
        public static final String FIND_ALL = "Tag.getAllTags";
        public static final String LOCK_BY_ID = "Tag.deleteTag";

        public QueryNames() {
        }
    }
}
