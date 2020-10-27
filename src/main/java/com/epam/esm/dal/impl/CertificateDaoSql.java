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
import com.epam.esm.dal.exception.DaoException;
import com.epam.esm.dal.mapper.CertificateMapper;
import com.epam.esm.dal.mapper.CertificateWithTagsMapper;
import com.epam.esm.dal.util.DuplicateResultsRemover;
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
	private static final String sqlFindCertificatesByTagName = "SELECT giftcertificate.id, giftcertificate.name, description, price, createDate, lastUpdateDate, duration, tag.id, tag.name FROM giftcertificate JOIN `tag-certificate` ON giftcertificate.id = `tag-certificate`.idCertificate JOIN tag ON tag.id = `tag-certificate`.idTag WHERE tag.name = (?);";
	private static final String sqlSortCertificatesByCreationDateASC = "SELECT giftcertificate.id, giftcertificate.name, description, price, createDate, lastUpdateDate, duration, tag.id, tag.name FROM giftcertificate JOIN `tag-certificate` ON giftcertificate.id = `tag-certificate`.idCertificate JOIN tag ON tag.id = `tag-certificate`.idTag ORDER BY createDate;";
	private static final String sqlSortCertificatesByCreationDateDESC = "SELECT giftcertificate.id, giftcertificate.name, description, price, createDate, lastUpdateDate, duration, tag.id, tag.name FROM giftcertificate JOIN `tag-certificate` ON giftcertificate.id = `tag-certificate`.idCertificate JOIN tag ON tag.id = `tag-certificate`.idTag ORDER BY createDate DESC;";
	private static final String sqlSortCertificatesByNameASC = "SELECT giftcertificate.id, giftcertificate.name, description, price, createDate, lastUpdateDate, duration, tag.id, tag.name FROM giftcertificate JOIN `tag-certificate` ON giftcertificate.id = `tag-certificate`.idCertificate JOIN tag ON tag.id = `tag-certificate`.idTag ORDER BY giftcertificate.name;";
	private static final String sqlSortCertificatesByNameDESC = "SELECT giftcertificate.id, giftcertificate.name, description, price, createDate, lastUpdateDate, duration, tag.id, tag.name FROM giftcertificate JOIN `tag-certificate` ON giftcertificate.id = `tag-certificate`.idCertificate JOIN tag ON tag.id = `tag-certificate`.idTag ORDER BY giftcertificate.name DESC;";
	private static final String sqlFindCertificatesByPartOfName = "SELECT giftcertificate.id, giftcertificate.name, description, price, createDate, lastUpdateDate, duration, tag.id, tag.name FROM giftcertificate JOIN `tag-certificate` ON giftcertificate.id = `tag-certificate`.idCertificate JOIN tag ON tag.id = `tag-certificate`.idTag WHERE giftcertificate.name like (?);";
	private static final String sqlFindCertificatesByDescription = "SELECT giftcertificate.id, giftcertificate.name, description, price, createDate, lastUpdateDate, duration, tag.id, tag.name FROM giftcertificate JOIN `tag-certificate` ON giftcertificate.id = `tag-certificate`.idCertificate JOIN tag ON tag.id = `tag-certificate`.idTag WHERE description like (?);";

	private static final String sortDirectionASC = "asc";
	private static final String sortDirectionDESC = "desc";
	private static final String percentSign = "%";

	@Autowired
	public CertificateDaoSql(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
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
			throw new DaoException("Exception when call addCertificate() from CertificateDaoSql", e);
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
	public List<GiftCertificate> findAllCertificates() {

		List<GiftCertificate> certificates = new ArrayList<GiftCertificate>();
		try {
			certificates = jdbcTemplate.query(sqlFindAllCertificates, new CertificateMapper());
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

	@Override
	public List<GiftCertificate> findCertificatesByTagName(String tagName) {

		List<GiftCertificate> certificates = new ArrayList<GiftCertificate>();

		try {
			certificates = jdbcTemplate.query(sqlFindCertificatesByTagName, new Object[] { tagName },
					new CertificateWithTagsMapper());
		} catch (DataAccessException e) {
			// nothing was found by the request;
			return certificates;
		}
		return certificates;
	}

	@Override
	public List<GiftCertificate> findCertificatesByPartOfName(String nameContains) {

		List<GiftCertificate> certificates = new ArrayList<GiftCertificate>();

		try {
			certificates = jdbcTemplate.query(sqlFindCertificatesByPartOfName,
					new Object[] { percentSign + nameContains + percentSign }, new CertificateWithTagsMapper());

		} catch (DataAccessException e) {
			// nothing was found by the request;
			return certificates;
		}
		return DuplicateResultsRemover.removeDuplicateResults(certificates);
	}

	@Override
	public List<GiftCertificate> findCertificatesByDescription(String description) {

		List<GiftCertificate> certificates = new ArrayList<GiftCertificate>();

		try {
			certificates = jdbcTemplate.query(sqlFindCertificatesByDescription,
					new Object[] { percentSign + description + percentSign }, new CertificateWithTagsMapper());

		} catch (DataAccessException e) {
			// nothing was found by the request;
			return certificates;
		}
		return DuplicateResultsRemover.removeDuplicateResults(certificates);
	}

//	@Override
//	public List<GiftCertificate> sortCertificatesByDate(String sortDirection) throws DaoException {
//
//		List<GiftCertificate> certificates;
//
//		try {
//			if (sortDirection.equals(sortDirectionASC)) {
//				certificates = jdbcTemplate.query(sqlSortCertificatesByCreationDateASC,
//						new CertificateWithTagsMapper());
//				return certificates;
//			}
//			certificates = jdbcTemplate.query(sqlSortCertificatesByCreationDateDESC, new CertificateWithTagsMapper());
//		} catch (DataAccessException e) {
//			throw new DaoException("Exception when call sortCertificatesByDate() from CertificateDaoSql", e);
//		}
//		return certificates;
//	}
//
//	@Override
//	public List<GiftCertificate> sortCertificatesByName(String sortDirection) throws DaoException {
//
//		List<GiftCertificate> certificates;
//
//		try {
//			if (sortDirection.equals(sortDirectionDESC)) {
//				certificates = jdbcTemplate.query(sqlSortCertificatesByNameDESC, new CertificateWithTagsMapper());
//				return certificates;
//			}
//			certificates = jdbcTemplate.query(sqlSortCertificatesByNameASC, new CertificateWithTagsMapper());
//		} catch (DataAccessException e) {
//			throw new DaoException("Exception when call sortCertificatesByName() from CertificateDaoSql", e);
//		}
//		return certificates;
//	}

}
