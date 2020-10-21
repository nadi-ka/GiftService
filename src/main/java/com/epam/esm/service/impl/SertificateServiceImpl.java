package com.epam.esm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.esm.dal.SertificateDao;
import com.epam.esm.dal.exception.DaoException;
import com.epam.esm.entity.GiftSertificate;
import com.epam.esm.service.SertificateService;
import com.epam.esm.service.ServiceException;

@Service
public class SertificateServiceImpl implements SertificateService {
	
	@Autowired
	SertificateDao sertificateDao;

	@Override
	public List<GiftSertificate> getSertificates() throws ServiceException {
		
		List<GiftSertificate> sertificates;
		try {
			sertificates = sertificateDao.findAllSertificates();
		} catch (DaoException e) {
			throw new ServiceException("Exception when call getSertificates() from SertificateServiceImpl", e);
		}
		return sertificates;
	}

	@Override
	public void saveSertificate(GiftSertificate theSertificate) throws ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public GiftSertificate getSertificate(long theId) throws ServiceException {

		GiftSertificate sertificate;
		try {
			sertificate = sertificateDao.findSertificate(theId);
		} catch (DaoException e) {
			throw new ServiceException("Exception when call getSertificate() from SertificateServiceImpl", e);
		}
		return sertificate;
	}

	@Override
	public void deleteSertificate(long theId) throws ServiceException {
		// TODO Auto-generated method stub

	}

}
