package com.epam.esm.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.dal.CertificateDao;
import com.epam.esm.dal.mapper.CertificateMapper;
import com.epam.esm.dal.mapper.CertificateWithTagsMapper;
import com.epam.esm.dal.util.DuplicateResultsRemover;
import com.epam.esm.dal.util.SqlQueryBuilder;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.rest.parameter.FilterParam;
import com.epam.esm.rest.parameter.OrderParam;

@Repository
public class CertificateDaoSql implements CertificateDao {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	private SqlQueryBuilder sqlBuilder;

	private static final Logger log = LogManager.getLogger(CertificateDaoSql.class);

	private static final String sqlFindCertificateById = "SELECT * FROM giftCertificate WHERE Id = (?)";
	private static final String sqlAddCertificate = "INSERT INTO giftCertificate (Name, Description, Price, CreateDate, LastUpdateDate, Duration) VALUES (?,?,?,?,?,?);";
	private static final String sqlInsertIntoM2M = "INSERT INTO `tag-certificate` VALUES (?,?);";
	private static final String sqlUpdateCertificate = "Update giftCertificate set Name = (?), Description = (?), Price = (?), LastUpdateDate = (?), Duration = (?) where Id = (?);";
	private static final String sqlDeleteCertificateById = "DELETE FROM giftCertificate WHERE Id = (?);";
	private static final String sqlDeleteFromM2M = "DELETE FROM `tag-certificate` WHERE IdCertificate = (?);";

	@Autowired
	public CertificateDaoSql(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Transactional
	@Override
	public GiftCertificate addCertificate(GiftCertificate certificate) {

		LocalDateTime lastUpdateDate = certificate.getLastUpdateDate();
		Timestamp lastUpdateTimestamp = ((lastUpdateDate == null) ? null : Timestamp.valueOf(lastUpdateDate));

		KeyHolder keyHolder = new GeneratedKeyHolder();

		// add certificate to the DB table and return generated id;
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {

				String paramToReturn = "Id";

				PreparedStatement preparedStatement = con.prepareStatement(sqlAddCertificate,
						new String[] { paramToReturn });
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
			jdbcTemplate.update(sqlInsertIntoM2M, tag.getId(), newSertificateId);
		}
		return certificate;
	}

	@Override
	public GiftCertificate updateCertificate(GiftCertificate certificate) {

		jdbcTemplate.update(sqlUpdateCertificate, certificate.getName(), certificate.getDescription(),
				certificate.getPrice(), certificate.getLastUpdateDate(), certificate.getDuration(),
				certificate.getId());

		return certificate;
	}

	@Override
	public List<GiftCertificate> findCertificates(List<FilterParam> filterParams, List<OrderParam> orderParams) {

		List<GiftCertificate> certificates = new ArrayList<GiftCertificate>();
		String sqlQuery = sqlBuilder.buildFindCertificatesQuery(filterParams, orderParams);

		try {
			certificates = jdbcTemplate.query(sqlQuery, new CertificateWithTagsMapper());
			certificates = DuplicateResultsRemover.removeDuplicateResults(certificates);

		} catch (DataAccessException e) {
			// nothing was found by the request;
			return certificates;
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

	@Transactional
	@Override
	public void deleteCertificate(long id) {

		int[] types = { Types.BIGINT };
		// delete the data from M2M table;
		jdbcTemplate.update(sqlDeleteFromM2M, new Object[] { id }, types);

		// delete the certificate from giftCetrificate table;
		jdbcTemplate.update(sqlDeleteCertificateById, new Object[] { id }, types);

	}

}
