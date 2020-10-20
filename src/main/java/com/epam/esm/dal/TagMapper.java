package com.epam.esm.dal;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.epam.esm.dal.constant.ColumnNameHolder;
import com.epam.esm.entity.Tag;

public class TagMapper implements RowMapper<Tag> {

	@Override
	public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Tag tag = new Tag();
		tag.setId(rs.getLong(ColumnNameHolder.TAG_ID));
		tag.setName(rs.getString(ColumnNameHolder.TAG_NAME));
		return tag;
	}

}
