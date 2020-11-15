package com.epam.esm.domain;

import com.epam.esm.dao.audit.AuditCertificateListener;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@EntityListeners(AuditCertificateListener.class)
@Getter
@Setter
@ToString(of = {"id", "name", "description", "price", "duration", "createDate"})
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "certificate")
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_certificate")
    private Long id;

    @Column(name = "name_certificate")
    private String name;

    @Column
    private String description;

    @Column
    private Double price;

    @Column(name = "creation_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @Column(name = "update_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdateDate;

    @Column(name = "lock_certificate")
    private Integer lock;

    @Column(name = "duration")
    private Integer duration;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tag_certificate",
            joinColumns = @JoinColumn(name = "certificate_id", referencedColumnName = "id_certificate"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id_tag"))
    private List<Tag> tags;

    @ManyToMany(mappedBy = "certificates", fetch = FetchType.LAZY)
    private List<Order> orders;
}
