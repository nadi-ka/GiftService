package com.epam.esm.dal.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.epam.esm.dal.constant.ColumnNameHolder;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

public class TagWithCertificatesMapper implements RowMapper<Tag>{

	@Override
	public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Tag tag = new Tag();
		List<GiftCertificate> certificates = new ArrayList<GiftCertificate>();
		
		tag.setId(rs.getLong(ColumnNameHolder.TAG_ID));
		tag.setName(rs.getString(ColumnNameHolder.TAG_NAME));
		
		while (rs.next()) {
			GiftCertificate certificate = new GiftCertificate();
			
			certificate.setId(rs.getLong(ColumnNameHolder.CERTIFICATE_ID));
			certificate.setName(rs.getString(ColumnNameHolder.CERTIFICATE_NAME));
			certificate.setDescription(rs.getString(ColumnNameHolder.CERTIFICATE_DESCRIPTION));
			certificate.setPrice(rs.getDouble(ColumnNameHolder.CERTIFICATE_PRICE));
			certificate.setCreationDate(rs.getTimestamp(ColumnNameHolder.CERTIFICATE_CREATION_DATE).toLocalDateTime());
			Timestamp lastUpdateDate = rs.getTimestamp(ColumnNameHolder.CERTIFICATE_UPDATE_DATE);
			LocalDateTime lastUpdateDateTime = ((lastUpdateDate == null) ? null : lastUpdateDate.toLocalDateTime());
			certificate.setLastUpdateDate(lastUpdateDateTime);
			certificate.setDuration(rs.getInt(ColumnNameHolder.CERTIFICATE_DURATION));

			certificates.add(certificate);
			certificate = null;
			
		}
		tag.setCertificates(certificates);
		
		return tag;
	}

}
