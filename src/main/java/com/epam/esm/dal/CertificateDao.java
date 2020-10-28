package com.epam.esm.dal;

import java.util.List;

import com.epam.esm.dal.exception.DaoException;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.rest.parameter.FilterParam;
import com.epam.esm.rest.parameter.OrderParam;

public interface CertificateDao {

	GiftCertificate addCertificate(GiftCertificate certificate);

	GiftCertificate updateCertificate(GiftCertificate certificate);

	List<GiftCertificate> findCertificates(List<FilterParam> filterParams, List<OrderParam> orderParams);

	GiftCertificate findCertificate(long id);

	void deleteCertificate(long id);

}
