package com.epam.esm.service;

import java.util.List;

import com.epam.esm.dto.GiftCertificateCreateUpdateDTO;
import com.epam.esm.dto.GiftCertificateGetDTO;
import com.epam.esm.service.exception.ServiceException;

public interface CertificateService {

	List<GiftCertificateGetDTO> getCertificates();

	GiftCertificateGetDTO getCertificate(long theId);

	GiftCertificateGetDTO saveCertificate(GiftCertificateCreateUpdateDTO theCertificate)
			throws ServiceException;

	GiftCertificateGetDTO updateCertificate(GiftCertificateCreateUpdateDTO theCertificate)
			throws ServiceException;

	void deleteCertificate(long theId) throws ServiceException;

	List<GiftCertificateGetDTO> getCertificatesByTagName(String tagName);

	List<GiftCertificateGetDTO> getCertificatesByPartOfName(String nameContains);

	List<GiftCertificateGetDTO> getCertificatesByDescription(String description);

//	List<GiftCertificateGetDTO> getCertificatesSorted(String sortBy, String sortDirection) throws ServiceException;
	
	List<GiftCertificateGetDTO> sortCertificate(List<GiftCertificateGetDTO> certificates, String sortBy);

}
