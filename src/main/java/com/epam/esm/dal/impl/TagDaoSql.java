package com.epam.esm.dal.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.epam.esm.dal.TagDao;
import com.epam.esm.dal.constant.ColumnNameHolder;
import com.epam.esm.dal.exception.DaoException;
import com.epam.esm.dal.pool_source.PoolSource;
import com.epam.esm.entity.Tag;

@Component
public class TagDaoSql implements TagDao {

	private final JdbcTemplate jdbcTemplate;

	private final static String sqlFindAllTags = "SELECT * FROM tag;";
	
	private static final Logger log = LogManager.getLogger(TagDaoSql.class);

	public TagDaoSql(PoolSource poolSource) {
		this.jdbcTemplate = new JdbcTemplate(poolSource.getDataSource());
		log.info("in the constructor TagDaoSql");
	}

	@Override
	public int addTag(String tagName) throws DaoException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void updateTag(long id, String tagName) throws DaoException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Tag> findAllTags() throws DaoException {
		
//		try {
//		Context initContext = new InitialContext();
//        Context envContext  = (Context)initContext.lookup("java:/comp/env");
//
//		DataSource ds = (DataSource) envContext.lookup("jdbc/mjc-giftService");
//		Connection conn = ds.getConnection();
//
//		Statement statement = conn.createStatement();
//		String sql = "select * from tag";
//		ResultSet rs = statement.executeQuery(sql);
//
//		List<Tag> tags = new ArrayList<Tag>();
//
//		while (rs.next()) {
//			Long id = rs.getLong("Id");
//			String tagName = rs.getString("Name");
//			Tag tag = new Tag();
//			tag.setId(id);
//			tag.setName(tagName);
//			tags.add(tag);
//		}
		
		
		return jdbcTemplate.query(sqlFindAllTags, ROW_MAPPER);

	}

	@Override
	public void deleteTag(long id) throws DaoException {
		// TODO Auto-generated method stub

	}
	
	RowMapper<Tag> ROW_MAPPER = (ResultSet resultSet, int rowNum) -> {
        return new Tag(resultSet.getLong(ColumnNameHolder.TAG_ID), resultSet.getString(ColumnNameHolder.TAG_NAME));
    };

}
