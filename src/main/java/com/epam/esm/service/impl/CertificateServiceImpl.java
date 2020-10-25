package com.epam.esm.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.esm.dal.CertificateDao;
import com.epam.esm.dal.exception.DaoException;
import com.epam.esm.dto.GiftCertificateCreateUpdateDTO;
import com.epam.esm.dto.GiftCertificateGetDTO;
import com.epam.esm.dto.GiftCertificateGetForCreationDTO;
import com.epam.esm.dto.GiftCertificateGetForUpdateDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.util.DateTimeFormatterISO;

@Service
public class CertificateServiceImpl implements CertificateService {

	@Autowired
	private CertificateDao certificateDao;

	@Autowired
	private ModelMapper modelMapper;
	
	private static final Logger log = LogManager.getLogger(CertificateServiceImpl.class);

	@Override
	public List<GiftCertificateGetDTO> getCertificates() throws ServiceException {

		List<GiftCertificate> certificates;
		try {
			certificates = certificateDao.findAllCertificates();
		} catch (DaoException e) {
			throw new ServiceException("Exception when call getCertificates() from CertificateServiceImpl", e);
		}
		return certificates.stream().map(this::convertToDto).collect(Collectors.toList());
	}

	@Override
	public GiftCertificateGetDTO getCertificate(long theId) {

		GiftCertificate certificate = certificateDao.findCertificate(theId);
		
		if (certificate == null) {
			return null;
		}
		return convertToDto(certificate);
	}

	@Override
	public GiftCertificateGetForCreationDTO saveCertificate(GiftCertificateCreateUpdateDTO theCertificate) throws ServiceException {

		GiftCertificate certificateToAdd = convertToEntity(theCertificate);
		
		// set the creation Date and Time(now) and format in accordance with ISO-8601
		
		certificateToAdd.setCreationDate(DateTimeFormatterISO.createAndformatDateTime());
		
		GiftCertificate addedCertificate;
		try {
			addedCertificate = certificateDao.addCertificate(certificateToAdd);
		} catch (DaoException e) {
			throw new ServiceException("Exception when calling saveCertificate() from CertificateServiceImpl", e);
		}
		return convertToDtoForCreationOperation(addedCertificate);
	}

	@Override
	public GiftCertificateGetForUpdateDTO updateCertificate(GiftCertificateCreateUpdateDTO theCertificate) throws ServiceException {

		GiftCertificate certificateToUpdate = convertToEntity(theCertificate);
		
		// set LastUpdateDate and Time(now) and format in accordance with ISO-8601
		certificateToUpdate.setLastUpdateDate(DateTimeFormatterISO.createAndformatDateTime());
		
		GiftCertificate updatedCertificate;
		try {
			updatedCertificate = certificateDao.updateCertificate(certificateToUpdate);
		} catch (DaoException e) {
			throw new ServiceException("Exception when calling updateCertificate() from CertificateServiceImpl", e);
		}
		return convertToDtoForUpdationOperation(updatedCertificate);
	}

	@Override
	public void deleteCertificate(long theId) throws ServiceException {

		try {
			certificateDao.deleteCertificate(theId);
		} catch (DaoException e) {
			throw new ServiceException("The certificate wasn't deleted.", e);
		}
	}

//	@Override
//	public List<GiftCertificateDTO> getAllCertificatesByTagName(String tagName) {
//		
//		Tag tag = tagDao.findAllCertificatesByTagId(theId);
//		if (tag == null) {
//			return null;
//		}
//		return convertToDto(tag);
//	}

	private GiftCertificateGetDTO convertToDto(GiftCertificate giftCertificate) {
		
		if (giftCertificate.getTags() == null) {
			giftCertificate.setTags(Collections.emptyList());
		}

		GiftCertificateGetDTO certificateDTO = modelMapper.map(giftCertificate, GiftCertificateGetDTO.class);

		return certificateDTO;
	}
	
	private GiftCertificateGetForCreationDTO convertToDtoForCreationOperation(GiftCertificate giftCertificate) {
		
		if (giftCertificate.getTags() == null) {
			giftCertificate.setTags(Collections.emptyList());
		}

		GiftCertificateGetForCreationDTO certificateDTO = modelMapper.map(giftCertificate, GiftCertificateGetForCreationDTO.class);

		return certificateDTO;
	}
	
	private GiftCertificateGetForUpdateDTO convertToDtoForUpdationOperation(GiftCertificate giftCertificate) {
		
		if (giftCertificate.getTags() == null) {
			giftCertificate.setTags(Collections.emptyList());
		}

		GiftCertificateGetForUpdateDTO certificateDTO = modelMapper.map(giftCertificate, GiftCertificateGetForUpdateDTO.class);

		return certificateDTO;
	}

	private GiftCertificate convertToEntity(GiftCertificateCreateUpdateDTO giftDTO) {

		GiftCertificate certificate = modelMapper.map(giftDTO, GiftCertificate.class);

		return certificate;
	}

}
