package com.epam.esm.dal;

import java.util.List;

import com.epam.esm.dal.exception.DaoException;
import com.epam.esm.entity.GiftSertificate;

public interface SertificateDao {
	
	int addSertificate(GiftSertificate sertificate) throws DaoException;
	
	void updateSertificate(long id, GiftSertificate sertificate) throws DaoException;
	
	List<GiftSertificate> findAllSertificates() throws DaoException;
	
	GiftSertificate findSertificate(long id) throws DaoException;

	void deleteSertificate(long id) throws DaoException;

}
