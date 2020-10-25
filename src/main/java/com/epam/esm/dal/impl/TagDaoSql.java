package com.epam.esm.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.epam.esm.dal.TagDao;
import com.epam.esm.dal.constant.ColumnNameHolder;
import com.epam.esm.dal.exception.DaoException;
import com.epam.esm.dal.pool_source.PoolSource;
import com.epam.esm.entity.Tag;

@Repository
public class TagDaoSql implements TagDao {

	private final JdbcTemplate jdbcTemplate;

	private static final String sqlFindAllTags = "SELECT * FROM tag;";
	private static final String sqlFindTagById = "SELECT * FROM tag WHERE Id = (?)";
	private static final String sqlAddTag = "INSERT INTO tag (Name) VALUES (?)";
	private static final String sqlUpdateTag = "Update tag set Name = (?) where Id = (?);";
	private static final String sqlDeleteTagById = "DELETE FROM tag WHERE Id = (?);";
	private static final String sqlFindCertificateIdByTagId = "SELECT IdCertificate FROM `tag-certificate` WHERE IdTag = (?) LIMIT 1;";

	private static final Logger log = LogManager.getLogger(TagDaoSql.class);

	public TagDaoSql(PoolSource poolSource) {
		this.jdbcTemplate = new JdbcTemplate(poolSource.getDataSource());

	}

	@Override
	public Tag addTag(Tag tag) throws DaoException {

		KeyHolder keyHolder = new GeneratedKeyHolder();

		try {
			jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {

					PreparedStatement preparedStatement = con.prepareStatement(sqlAddTag, new String[] { "Id" });
					preparedStatement.setString(1, tag.getName());

					return preparedStatement;
				}
			}, keyHolder);
		} catch (DataAccessException e) {
			throw new DaoException("Exception when calling addTag() from TagDaoSql", e);
		}
		long newTagId = keyHolder.getKey().longValue();
		tag.setId(newTagId);

		return tag;
	}

	@Override
	public int updateTag(Tag tag) throws DaoException {

		int affectedRows = 0;
		try {
			affectedRows = jdbcTemplate.update(sqlUpdateTag, tag.getName(), tag.getId());
		} catch (DataAccessException e) {
			throw new DaoException("Exception when calling updateTag() from TagDaoSql", e);
		}
		return affectedRows;

	}

	@Override
	public List<Tag> findAllTags() throws DaoException {

		List<Tag> tags = new ArrayList<Tag>();
		try {
			tags = jdbcTemplate.query(sqlFindAllTags, ROW_MAPPER);
		} catch (DataAccessException e) {
			throw new DaoException("Exception when calling findAllTags() from TagDaoSql", e);
		}
		return tags;
	}

	@Override
	public Tag findTag(long id) {

		Tag tag;
		try {
			tag = jdbcTemplate.queryForObject(sqlFindTagById, new Object[] { id }, ROW_MAPPER);
		} catch (DataAccessException e) {
			tag = null;
		}
		return tag;
	}

	@Override
	public int deleteTag(long id) throws DaoException {

		int affectedRows = 0;
		try {
			int[] types = { Types.BIGINT };
			affectedRows = jdbcTemplate.update(sqlDeleteTagById, new Object[] { id }, types);
		} catch (DataAccessException e) {
			throw new DaoException("Exception when calling deleteTag() from TagDaoSql", e);
		}
		return affectedRows;

	}
	
	// The method is used for checking, if there is at least one certificate, 
	// bounded with given tag's Id
	
	@Override
	public long findCertificateIdByTagId(long tagId) {

		long certificateId;
		try {
			certificateId = (Long) jdbcTemplate.queryForObject(
		            sqlFindCertificateIdByTagId, new Object[] { tagId }, Long.class);
		} catch (DataAccessException e) {
			certificateId = 0;
		}
		return certificateId;
	}

	RowMapper<Tag> ROW_MAPPER = (ResultSet resultSet, int rowNum) -> {
		return new Tag(resultSet.getLong(ColumnNameHolder.TAG_ID), resultSet.getString(ColumnNameHolder.TAG_NAME));
	};

}
