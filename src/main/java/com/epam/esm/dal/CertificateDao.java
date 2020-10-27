package com.epam.esm.dal;

import java.util.List;

import com.epam.esm.dal.exception.DaoException;
import com.epam.esm.entity.GiftCertificate;

public interface CertificateDao {

	GiftCertificate addCertificate(GiftCertificate certificate) throws DaoException;

	GiftCertificate updateCertificate(GiftCertificate certificate) throws DaoException;

	List<GiftCertificate> findAllCertificates();

	GiftCertificate findCertificate(long id);

	void deleteCertificate(long id) throws DaoException;

	List<GiftCertificate> findCertificatesByTagName(String TagName);

//	List<GiftCertificate> sortCertificatesByDate(String sortDirection) throws DaoException;
//
//	List<GiftCertificate> sortCertificatesByName(String sortDirection) throws DaoException;

	List<GiftCertificate> findCertificatesByPartOfName(String nameContains);

	List<GiftCertificate> findCertificatesByDescription(String description);

}
