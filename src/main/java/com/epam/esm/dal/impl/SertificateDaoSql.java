package com.epam.esm.dal.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.epam.esm.dal.SertificateDao;
import com.epam.esm.dal.SertificateMapper;
import com.epam.esm.dal.exception.DaoException;
import com.epam.esm.dal.pool_source.PoolSource;
import com.epam.esm.entity.GiftSertificate;

@Repository
public class SertificateDaoSql implements SertificateDao {

	private final JdbcTemplate jdbcTemplate;

	private static final Logger log = LogManager.getLogger(SertificateDaoSql.class);

	private final static String sqlFindAllSertificates = "SELECT * FROM giftSertificate;";
	private final static String sqlFindSertificateById = "SELECT * FROM giftSertificate WHERE Id = (?)";

	public SertificateDaoSql(PoolSource poolSource) {
		this.jdbcTemplate = new JdbcTemplate(poolSource.getDataSource());
	}

	@Override
	public int addSertificate(GiftSertificate sertificate) throws DaoException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void updateSertificate(long id, GiftSertificate sertificate) throws DaoException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<GiftSertificate> findAllSertificates() throws DaoException {

		List<GiftSertificate> sertificates = null;
		try {
			sertificates = jdbcTemplate.query(sqlFindAllSertificates, new SertificateMapper());
		} catch (DataAccessException e) {
			throw new DaoException("Exception when call findAllSertificates() from SertificateDaoSql", e);
		}
		return sertificates;
	}

	@Override
	public GiftSertificate findSertificate(long id) throws DaoException {

		GiftSertificate sertificate;
		try {
			sertificate = jdbcTemplate.queryForObject(sqlFindSertificateById, new Object[] { id },
					new SertificateMapper());
		} catch (DataAccessException e) {
			throw new DaoException("Exception when call findSertificate() from SertificateDaoSql", e);
		}
		return sertificate;
	}

	@Override
	public void deleteSertificate(long id) throws DaoException {
		// TODO Auto-generated method stub

	}

}
