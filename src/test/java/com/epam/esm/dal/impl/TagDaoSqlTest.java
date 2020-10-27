/**
 * 
 */
package com.epam.esm.dal.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.epam.esm.dal.TagDao;
import com.epam.esm.dal.pool_source.PoolSource;
import com.epam.esm.entity.Tag;

class TagDaoSqlTest {

	private TagDao tagDao;

	private JdbcTemplate jdbcTemplate;


	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {	
		}


	@BeforeEach
	void setUp() {

		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.dal.impl.TagDaoSql#addTag(com.epam.esm.entity.Tag)}.
	 */
	@Test
	void testAddTag() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.dal.impl.TagDaoSql#updateTag(com.epam.esm.entity.Tag)}.
	 */
	@Test
	void testUpdateTag() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.epam.esm.dal.impl.TagDaoSql#findAllTags()}.
	 */
	@Test
	void testFindAllTags() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.epam.esm.dal.impl.TagDaoSql#findTag(long)}.
	 */
	@Test
	void testFindTag() {
//		Tag tagExpected = new Tag(1, "#Sport");
//		assertEquals(tagExpected, tagDao.findTag(1));

	}

	/**
	 * Test method for {@link com.epam.esm.dal.impl.TagDaoSql#deleteTag(long)}.
	 */
	@Test
	void testDeleteTag() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.dal.impl.TagDaoSql#findCertificateIdByTagId(long)}.
	 */
	@Test
	void testFindCertificateIdByTagId() {
		fail("Not yet implemented"); // TODO
	}

}
