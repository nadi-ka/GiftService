package com.epam.esm.service;

import java.util.List;

import com.epam.esm.dto.GiftCertificateCreateUpdateDTO;
import com.epam.esm.dto.GiftCertificateGetDTO;
import com.epam.esm.dto.GiftCertificateGetForCreationDTO;
import com.epam.esm.dto.GiftCertificateGetForUpdateDTO;
import com.epam.esm.service.exception.ServiceException;

public interface CertificateService {

	public List<GiftCertificateGetDTO> getCertificates() throws ServiceException;
	
	public GiftCertificateGetDTO getCertificate(long theId);

	public GiftCertificateGetForCreationDTO saveCertificate(GiftCertificateCreateUpdateDTO theCertificate) throws ServiceException;

	public GiftCertificateGetForUpdateDTO updateCertificate(GiftCertificateCreateUpdateDTO theCertificate) throws ServiceException;

	public void deleteCertificate(long theId) throws ServiceException;

}
