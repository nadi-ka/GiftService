package com.epam.esm.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.dal.CertificateDao;
import com.epam.esm.dal.exception.DaoException;
import com.epam.esm.dal.mapper.CertificateMapper;
import com.epam.esm.dal.mapper.TagWithCertificatesMapper;
import com.epam.esm.dal.pool_source.PoolSource;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

@Repository
public class CertificateDaoSql implements CertificateDao {

	private final JdbcTemplate jdbcTemplate;

	private static final Logger log = LogManager.getLogger(CertificateDaoSql.class);

	private static final String sqlFindAllCertificates = "SELECT * FROM giftCertificate;";
	private static final String sqlFindCertificateById = "SELECT * FROM giftCertificate WHERE Id = (?)";
	private static final String sqlAddCertificate = "INSERT INTO giftCertificate (Name, Description, Price, CreateDate, LastUpdateDate, Duration) VALUES (?,?,?,?,?,?);";
	private static final String sqlInsertIntoM2M = "INSERT INTO `tag-certificate` VALUES (?,?);";
	private static final String sqlUpdateCertificate = "Update giftCertificate set Name = (?), Description = (?), Price = (?), LastUpdateDate = (?), Duration = (?) where Id = (?);";
	private static final String sqlDeleteCertificateById = "DELETE FROM giftCertificate WHERE Id = (?);";
	private static final String sqlDeleteFromM2M = "DELETE FROM `tag-certificate` WHERE IdCertificate = (?);";
	private static final String sqlFindAllCertificatesByTagName = "SELECT giftcertificate.id, giftcertificate.name, description, price, createDate, lastUpdateDate, duration, tag.id, tag.name FROM giftcertificate JOIN `tag-certificate` ON giftcertificate.id = `tag-certificate`.idCertificate JOIN tag ON tag.id = `tag-certificate`.idTag WHERE tag.name = (?);";

	public CertificateDaoSql(PoolSource poolSource) {
		this.jdbcTemplate = new JdbcTemplate(poolSource.getDataSource());
	}

	@Transactional
	@Override
	public GiftCertificate addCertificate(GiftCertificate certificate) throws DaoException {

		LocalDateTime lastUpdateDate = certificate.getLastUpdateDate();
		Timestamp lastUpdateTimestamp = ((lastUpdateDate == null) ? null : Timestamp.valueOf(lastUpdateDate));

		KeyHolder keyHolder = new GeneratedKeyHolder();

		try {

			// add certificate to the DB table and return generated id;
			jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {

					PreparedStatement preparedStatement = con.prepareStatement(sqlAddCertificate,
							new String[] { "Id" });
					preparedStatement.setString(1, certificate.getName());
					preparedStatement.setString(2, certificate.getDescription());
					preparedStatement.setDouble(3, certificate.getPrice());
					preparedStatement.setTimestamp(4, Timestamp.valueOf(certificate.getCreationDate()));
					preparedStatement.setTimestamp(5, lastUpdateTimestamp);
					preparedStatement.setLong(6, certificate.getDuration());

					return preparedStatement;
				}
			}, keyHolder);

			long newSertificateId = keyHolder.getKey().longValue();
			certificate.setId(newSertificateId);

			// add tagId and certificateId to the M2M table;

			for (Tag tag : certificate.getTags()) {
				int insertedRowM2M = jdbcTemplate.update(sqlInsertIntoM2M, tag.getId(), newSertificateId);

				// (insertedRowM2M == 0) means that the data wasn't added to the table;

				if (insertedRowM2M == 0) {
					throw new DaoException(
							"Exception when inserting data into the table `tag-certificate`, the data wasn't added");
				}
			}

		} catch (DataAccessException e) {
			throw new DaoException("Exception when call findAllSertificates() from CertificateDaoSql", e);
		}
		return certificate;
	}

	@Override
	public GiftCertificate updateCertificate(GiftCertificate certificate) throws DaoException {

		int affectedRows = 0;
		try {
			affectedRows = jdbcTemplate.update(sqlUpdateCertificate, certificate.getName(),
					certificate.getDescription(), certificate.getPrice(), certificate.getLastUpdateDate(),
					certificate.getDuration(), certificate.getId());
		} catch (DataAccessException e) {
			throw new DaoException("Exception when call updateCertificate() from CertificateDaoSql", e);
		}
		if (affectedRows == 0) {
			throw new DaoException("The certificate wasn't updated");
		}
		return certificate;
	}

	@Override
	public List<GiftCertificate> findAllCertificates() throws DaoException {

		List<GiftCertificate> certificates = new ArrayList<GiftCertificate>();
		try {
			certificates = jdbcTemplate.query(sqlFindAllCertificates, new CertificateMapper());
		} catch (DataAccessException e) {
			throw new DaoException("Exception when call findAllSertificates() from CertificateDaoSql", e);
		}
		return certificates;
	}

	@Override
	public GiftCertificate findCertificate(long id) {

		GiftCertificate certificate;
		try {
			certificate = jdbcTemplate.queryForObject(sqlFindCertificateById, new Object[] { id },
					new CertificateMapper());
		} catch (DataAccessException e) {
			certificate = null;
		}
		return certificate;
	}

	@Override
	public List<GiftCertificate> findAllCertificatesByTagName(String tagName) {

		Tag tag;
		try {
			tag = jdbcTemplate.queryForObject(sqlFindAllCertificatesByTagName, new Object[] { tagName },
					new TagWithCertificatesMapper());
		} catch (DataAccessException e) {
			return Collections.emptyList();
		}
		return tag.getCertificates();
	}

	@Transactional
	@Override
	public void deleteCertificate(long id) throws DaoException {

		try {
			int[] types = { Types.BIGINT };
			// delete the data from M2M table;
			jdbcTemplate.update(sqlDeleteFromM2M, new Object[] { id }, types);

			// delete the certificate from giftCetrificate table;
			jdbcTemplate.update(sqlDeleteCertificateById, new Object[] { id }, types);
		} catch (DataAccessException e) {
			throw new DaoException("Exception when calling deleteCertificate() from CertificateDaoSql", e);
		}
	}

}
