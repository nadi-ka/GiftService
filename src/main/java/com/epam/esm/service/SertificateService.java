package com.epam.esm.service;

import java.util.List;

import com.epam.esm.entity.GiftSertificate;

public interface SertificateService {

	public List<GiftSertificate> getSertificates() throws ServiceException;

	public void saveSertificate(GiftSertificate theSertificate) throws ServiceException;

	public GiftSertificate getSertificate(long theId) throws ServiceException;

	public void deleteSertificate(long theId) throws ServiceException;

}
