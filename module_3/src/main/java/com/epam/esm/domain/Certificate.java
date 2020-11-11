package com.epam.esm.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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
    @Column(name = "id_certificate", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name_certificate", unique = true)
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

    @ManyToMany(mappedBy = "certificates", fetch = FetchType.LAZY)
    private List<Tag> tags;

    @ManyToMany(mappedBy = "certificates", fetch = FetchType.LAZY)
    private List<Order> orders;
}
