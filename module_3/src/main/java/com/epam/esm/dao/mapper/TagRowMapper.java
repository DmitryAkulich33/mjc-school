package com.epam.esm.dao.mapper;

import com.epam.esm.domain.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TagRowMapper implements RowMapper<Tag> {
    private static final String ID = "id_tag";
    private static final String NAME = "name_tag";
    private static final String LOCK = "lock_tag";

    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tag tag = new Tag();

        tag.setId(rs.getLong(ID));
        tag.setName(rs.getString(NAME).trim());
        tag.setLock(rs.getInt(LOCK));
        return tag;
    }
}
