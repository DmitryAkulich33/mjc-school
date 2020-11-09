package com.epam.esm.domain;

import com.epam.esm.view.View;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "certificate")
@NamedQueries({
        @NamedQuery(name = Certificate.QueryNames.LOCK_BY_ID,
                query = "UPDATE certificate SET lock_certificate=1 WHERE id_certificate=:idCertificate"),
        @NamedQuery(name = Certificate.QueryNames.FIND_BY_ID,
                query = "SELECT c FROM certificate c WHERE lock_certificate=0 AND id_certificate=:idCertificate"),
})
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_certificate", updatable = false, nullable = false)
    @JsonView(View.V1.class)
    private Long id;

    @JsonView(View.V1.class)
    @Column(name = "name_certificate", unique = true)
    private String name;

    @JsonView(View.V1.class)
    @Column
    private String description;

    @JsonView(View.V1.class)
    @Column
    private Double price;

    @JsonView(View.V1.class)
    @Column(name = "creation_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @JsonView(View.V1.class)
    @Column(name = "update_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdateDate;

    @JsonView(View.V1.class)
    @Column(name = "lock_certificate")
    private Integer lock;

    @JsonView(View.V1.class)
    @Column(name = "duration")
    private Integer duration;

    @JsonView(View.V1.class)
    @ManyToMany(mappedBy = "certificates", fetch = FetchType.LAZY)
    private List<Tag> tags;

    @ManyToMany(mappedBy = "certificates", fetch = FetchType.LAZY)
    private List<Order> orders;

    public static final class QueryNames {
        public static final String FIND_BY_ID = "Certificate.getById";
        public static final String LOCK_BY_ID = "Certificate.deleteTag";

        public QueryNames() {
        }
    }
}
