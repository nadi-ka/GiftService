package com.epam.esm.dal.impl;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.epam.esm.dal.TagDao;
import com.epam.esm.dal.constant.ColumnNameHolder;
import com.epam.esm.dal.exception.DaoException;
import com.epam.esm.dal.pool_source.PoolSource;
import com.epam.esm.entity.Tag;

@Repository
public class TagDaoSql implements TagDao {

	private final JdbcTemplate jdbcTemplate;

	private final static String sqlFindAllTags = "SELECT * FROM tag;";
	private final static String sqlFindTagById = "SELECT * FROM tag WHERE Id = (?)";
	private static final String sqlAddTag = "INSERT INTO tag (Name) VALUES (?)";
	private static final String sqlUpdateTag = "Update tag set Name = (?) where Id = (?);";
	private static final String sqlDeleteTagById = "DELETE FROM tag WHERE Id = (?);";

	private static final Logger log = LogManager.getLogger(TagDaoSql.class);

	public TagDaoSql(PoolSource poolSource) {
		this.jdbcTemplate = new JdbcTemplate(poolSource.getDataSource());

	}

	@Override
	public int addTag(Tag tag) throws DaoException {
		int insertedRows = 0;
		try {
			insertedRows = jdbcTemplate.update(sqlAddTag, tag.getName());
		} catch (DataAccessException e) {
			throw new DaoException("Exception when calling addTag() from TagDaoSql", e);
		}
		return insertedRows;
	}

	@Override
	public int updateTag(Tag tag) throws DaoException {
		
		int affectedRows = 0;
		try {
			affectedRows = jdbcTemplate.update(sqlUpdateTag, tag.getName(), tag.getId());
		}catch (DataAccessException e) {
			throw new DaoException("Exception when calling updateTag() from TagDaoSql", e);
		}
		return affectedRows;

	}

	@Override
	public List<Tag> findAllTags() throws DaoException {

		List<Tag> tags;
		try {
			tags = jdbcTemplate.query(sqlFindAllTags, ROW_MAPPER);
		} catch (DataAccessException e) {
			throw new DaoException("Exception when calling findAllTags() from TagDaoSql", e);
		}
		return tags;
	}

	@Override
	public Tag findTag(long id) throws DaoException {

		Tag tag = null;
		try {
			tag = jdbcTemplate.queryForObject(sqlFindTagById, new Object[] { id }, ROW_MAPPER);
		}catch (DataAccessException e) {
			throw new DaoException("Exception when calling findTag() from TagDaoSql", e);
		}
		return tag;
	}

	@Override
	public int deleteTag(long id) throws DaoException {
		
		int affectedRows = 0;
		try {
			int[] types = {Types.BIGINT};
			affectedRows = jdbcTemplate.update(sqlDeleteTagById, new Object[] {id}, types);
		} catch (DataAccessException e) {
			throw new DaoException("Exception when calling deleteTag() from TagDaoSql", e);
		}
		return affectedRows;

	}

	RowMapper<Tag> ROW_MAPPER = (ResultSet resultSet, int rowNum) -> {
		return new Tag(resultSet.getLong(ColumnNameHolder.TAG_ID), resultSet.getString(ColumnNameHolder.TAG_NAME));
	};

}
