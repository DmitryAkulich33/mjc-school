package com.epam.esm.dao.builder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
public class CertificateQueryBuilder {
    private static final String FIND_TAG_BY_NAME = " JOIN tag_certificate ON certificate.id_certificate=tag_certificate.certificate_id " +
            "JOIN tag ON tag_certificate.tag_id=tag.id_tag WHERE tag.";
    private static final String FIELD_NAME_TAG = "name_tag=";
    private static final String AND = " AND ";
    private static final String TAG_UNLOCK = "tag.lock_tag=0";
    private static final String WHERE = " WHERE ";
    private static final String CERTIFICATE = " certificate.";
    private static final String LIKE = " LIKE '%";
    private static final String ORDER_BY = " ORDER BY ";
    private static final String NAME_CERTIFICATE = "name_certificate";
    private static final String DESCRIPTION = "description";
    private static final String OR = " OR ";
    private static final String SINGLE_QUOTE = "'";
    private static final String UNDERSCORES = "_";
    private static final String BRACKET_LEFT = "(";
    private static final String BRACKET_RIGHT = ")";
    private static final String PERCENT_SIGN = "%";


    public StringBuilder buildTagNameQuery(String name) {
        StringBuilder tagNameQuery = new StringBuilder();

        if (isNotBlank(name)) {
            String valueNameTag = StringUtils.wrap(name, SINGLE_QUOTE);

            tagNameQuery.append(FIND_TAG_BY_NAME)
                    .append(FIELD_NAME_TAG)
                    .append(valueNameTag)
                    .append(AND)
                    .append(TAG_UNLOCK)
                    .append(AND);
        } else {
            tagNameQuery.append(WHERE);
        }

        return tagNameQuery;
    }

    public StringBuilder buildSearchQuery(String search) {
        StringBuilder searchQuery = new StringBuilder();

        if (isNotBlank(search)) {

            searchQuery.append(BRACKET_LEFT)
                    .append(CERTIFICATE)
                    .append(NAME_CERTIFICATE)
                    .append(LIKE)
                    .append(search)
                    .append(PERCENT_SIGN)
                    .append(SINGLE_QUOTE)
                    .append(OR)
                    .append(CERTIFICATE)
                    .append(DESCRIPTION)
                    .append(LIKE)
                    .append(search)
                    .append(PERCENT_SIGN)
                    .append(SINGLE_QUOTE)
                    .append(BRACKET_RIGHT)
                    .append(AND);
        }

        return searchQuery;
    }

    public StringBuilder buildSortQuery(String sort) {
        StringBuilder orderQuery = new StringBuilder();

        if (isNotBlank(sort)) {
            String[] fields = sort.trim().split(UNDERSCORES);
            String sortType = fields[fields.length - 1].toUpperCase();
            orderQuery.append(ORDER_BY)
                    .append(CERTIFICATE)
                    .append(sort.trim().replace(String.format("%s%s", UNDERSCORES, sortType.toLowerCase()), StringUtils.EMPTY))
                    .append(StringUtils.SPACE)
                    .append(sortType);
        }

        return orderQuery;
    }
}
