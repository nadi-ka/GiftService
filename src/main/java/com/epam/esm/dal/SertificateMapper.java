package com.epam.esm.dal;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.jdbc.core.RowMapper;

import com.epam.esm.dal.constant.ColumnNameHolder;
import com.epam.esm.entity.GiftSertificate;

public class SertificateMapper implements RowMapper<GiftSertificate> {

	@Override
	public GiftSertificate mapRow(ResultSet rs, int rowNum) throws SQLException {

		GiftSertificate sertificate = new GiftSertificate();
		
		sertificate.setId(rs.getLong(ColumnNameHolder.SERTIFICATE_ID));
		sertificate.setName(rs.getString(ColumnNameHolder.SERTIFICATE_NAME));
		sertificate.setDescription(rs.getString(ColumnNameHolder.SERTIFICATE_DESCRIPTION));
		sertificate.setPrice(new BigDecimal(rs.getDouble(ColumnNameHolder.SERTIFICATE_PRICE)));
		sertificate.setCreationDate(rs.getTimestamp(ColumnNameHolder.SERTIFICATE_CREATION_DATE).toLocalDateTime());
		Timestamp lastUpdateDate = rs.getTimestamp(ColumnNameHolder.SERTIFICATE_UPDATE_DATE);
		LocalDateTime lastUpdateDateTime = ((lastUpdateDate == null) ? null : lastUpdateDate.toLocalDateTime());
		sertificate.setLastUpdateDate(lastUpdateDateTime);
		sertificate.setDuration(rs.getLong(ColumnNameHolder.SERTIFICATE_DURATION));

		return sertificate;
	}

}
