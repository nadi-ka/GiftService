package com.epam.esm.service;

import java.util.List;

import com.epam.esm.dto.GiftCertificateCreateUpdateDTO;
import com.epam.esm.dto.GiftCertificateGetDTO;
import com.epam.esm.dto.GiftCertificateGetForCreationDTO;
import com.epam.esm.dto.GiftCertificateGetForUpdateDTO;
import com.epam.esm.service.exception.ServiceException;

public interface CertificateService {

	List<GiftCertificateGetDTO> getCertificates() throws ServiceException;

	GiftCertificateGetDTO getCertificate(long theId);

	GiftCertificateGetForCreationDTO saveCertificate(GiftCertificateCreateUpdateDTO theCertificate)
			throws ServiceException;

	GiftCertificateGetForUpdateDTO updateCertificate(GiftCertificateCreateUpdateDTO theCertificate)
			throws ServiceException;

	void deleteCertificate(long theId) throws ServiceException;

	List<GiftCertificateGetDTO> getCertificatesByTagName(String tagName) throws ServiceException;

	List<GiftCertificateGetDTO> getCertificatesByPartOfName(String nameContains)
			throws ServiceException;
	
	List<GiftCertificateGetDTO> getCertificatesByDescription(String description)
			throws ServiceException;

	List<GiftCertificateGetDTO> getCertificatesSorted(String sortBy, String sortDirection) throws ServiceException;

}
