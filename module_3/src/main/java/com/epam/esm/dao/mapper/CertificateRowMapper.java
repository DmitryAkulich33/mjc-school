package com.epam.esm.dao.mapper;

import com.epam.esm.domain.Certificate;
import com.epam.esm.exceptions.ParseLocalDateTimeException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Component
public class CertificateRowMapper implements RowMapper<Certificate> {
    private static final String ID = "id_certificate";
    private static final String NAME = "name_certificate";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String CREATION_DATE = "creation_date";
    private static final String UPDATE_DATE = "update_date";
    private static final String LOCK = "lock_certificate";
    private static final String DURATION = "duration";

    @Override
    public Certificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        Certificate certificate = new Certificate();

        certificate.setId(rs.getLong(ID));
        certificate.setName(rs.getString(NAME));
        certificate.setDescription(rs.getString(DESCRIPTION));
        certificate.setPrice(rs.getDouble(PRICE));

        LocalDateTime creationDate = parseLocalDateTime(rs.getString(CREATION_DATE));
        certificate.setCreateDate(creationDate);

        String updateDate = rs.getString(UPDATE_DATE);
        if (isNotEmpty(updateDate)) {
            LocalDateTime updateDateTime = parseLocalDateTime(updateDate);
            certificate.setLastUpdateDate(updateDateTime);
        }

        certificate.setLock(rs.getInt(LOCK));
        certificate.setDuration(rs.getInt(DURATION));

        return certificate;
    }

    private LocalDateTime parseLocalDateTime(String text) {
        try {
            return LocalDateTime.parse(text.replace(" ", "T"));
        } catch (DateTimeParseException e) {
            throw new ParseLocalDateTimeException("message.wrong_date_format");
        }
    }
}
