package com.epam.esm.dao.specification;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Certificate_;
import com.epam.esm.domain.Tag;
import com.epam.esm.domain.Tag_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CertificateSpecification {
    public static Specification<Certificate> filter(String searchField, String tagName) {
        return Specification.where(partNameOrDescription(searchField))
                .and(tagName(tagName));
    }

    public static Specification<Certificate> partNameOrDescription(String searchField) {
        return (r, cq, cb) -> {
            if (searchField != null) {
                return cb.or(cb.like(r.get(Certificate_.description), "%" + searchField + "%"),
                        cb.like(r.get(Certificate_.name), "%" + searchField + "%"));

            }
            return cb.conjunction();
        };
    }

    public static Specification<Certificate> tagNames(List<String> tagNames) {
        return (r, cq, cb) -> {
            if (tagNames != null && !tagNames.isEmpty()) {
                List<Predicate> predicates = new ArrayList<>();
                for (String tagName : tagNames) {
                    Join<Certificate, Tag> join = r.join(Certificate_.tags, JoinType.INNER);
                    Predicate tagPredicate = cb.equal(join.get(Tag_.name), tagName);
                    predicates.add(tagPredicate);
                }
                return cb.and(predicates.toArray(new Predicate[0]));
            }
            return cb.conjunction();
        };
    }

    public static Specification<Certificate> tagName(String tagName) {
        return (r, cq, cb) -> {
            if (tagName != null) {
                Join<Certificate, Tag> join = r.join(Certificate_.tags, JoinType.INNER);
                return cb.equal(join.get(Tag_.name), tagName);
            }
            return cb.conjunction();
        };
    }
}
