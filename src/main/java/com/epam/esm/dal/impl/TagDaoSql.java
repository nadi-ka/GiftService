package com.epam.esm.dal.impl;

import java.sql.ResultSet;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.epam.esm.dal.TagDao;
import com.epam.esm.dal.TagMapper;
import com.epam.esm.dal.constant.ColumnNameHolder;
import com.epam.esm.dal.pool_source.PoolSource;
import com.epam.esm.entity.Tag;

@Component
public class TagDaoSql implements TagDao {

	private final JdbcTemplate jdbcTemplate;

	private final static String sqlFindAllTags = "SELECT * FROM tag;";
	private final static String sqlFindTagById = "SELECT * FROM tag WHERE ID = (?)";

	public TagDaoSql(PoolSource poolSource) {
		this.jdbcTemplate = new JdbcTemplate(poolSource.getDataSource());
		
	}

	@Override
	public int addTag(String tagName)  {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void updateTag(long id, String tagName) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Tag> findAllTags() {		
		
		return jdbcTemplate.query(sqlFindAllTags, ROW_MAPPER);

	}
	
	@Override
	public Tag findTag(long id) {
		
		Tag tag = jdbcTemplate.queryForObject(sqlFindTagById, new Object[] {id}, new TagMapper());
		return tag;
	}

	@Override
	public void deleteTag(long id) {
		// TODO Auto-generated method stub

	}
	
	RowMapper<Tag> ROW_MAPPER = (ResultSet resultSet, int rowNum) -> {
        return new Tag(resultSet.getLong(ColumnNameHolder.TAG_ID), resultSet.getString(ColumnNameHolder.TAG_NAME));
    };

}
