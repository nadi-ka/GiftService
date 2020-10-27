package com.epam.esm.service.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
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

	private static final String DATE_ASC = "date_asc";
	private static final String DATE_DESC = "date_desc";
	private static final String NAME_ASC = "name_asc";
	private static final String NAME_DESC = "name_desc";

	private static final Logger log = LogManager.getLogger(CertificateServiceImpl.class);

	@Override
	public List<GiftCertificateGetDTO> getCertificates() {

		List<GiftCertificate> certificates;
		certificates = certificateDao.findAllCertificates();

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
	public GiftCertificateGetDTO saveCertificate(GiftCertificateCreateUpdateDTO theCertificate)
			throws ServiceException {

		GiftCertificate certificateToAdd = convertToEntity(theCertificate);

		// set the creation Date and Time(now) and format in accordance with ISO-8601

		LocalDateTime creationTime = DateTimeFormatterISO.createAndformatDateTime();
		certificateToAdd.setCreationDate(creationTime);
		certificateToAdd.setLastUpdateDate(creationTime);

		GiftCertificate addedCertificate;
		try {
			addedCertificate = certificateDao.addCertificate(certificateToAdd);
		} catch (DaoException e) {
			throw new ServiceException("Exception when calling saveCertificate() from CertificateServiceImpl", e);
		}
		return convertToDto(addedCertificate);
	}

	@Override
	public GiftCertificateGetDTO updateCertificate(GiftCertificateCreateUpdateDTO theCertificate)
			throws ServiceException {

		GiftCertificate certificateToUpdate = convertToEntity(theCertificate);

		// set LastUpdateDate and Time(now) and format in accordance with ISO-8601
		certificateToUpdate.setLastUpdateDate(DateTimeFormatterISO.createAndformatDateTime());

		GiftCertificate updatedCertificate;
		try {
			updatedCertificate = certificateDao.updateCertificate(certificateToUpdate);
		} catch (DaoException e) {
			throw new ServiceException("Exception when calling updateCertificate() from CertificateServiceImpl", e);
		}
		return convertToDto(updatedCertificate);
	}

	@Override
	public void deleteCertificate(long theId) throws ServiceException {

		try {
			certificateDao.deleteCertificate(theId);
		} catch (DaoException e) {
			throw new ServiceException("The certificate wasn't deleted.", e);
		}
	}

	@Override
	public List<GiftCertificateGetDTO> getCertificatesByTagName(String tagName) {

		List<GiftCertificate> certificates;
		certificates = certificateDao.findCertificatesByTagName(tagName);

		return certificates.stream().map(this::convertToDto).collect(Collectors.toList());
	}

	@Override
	public List<GiftCertificateGetDTO> getCertificatesByPartOfName(String nameContains) {

		List<GiftCertificate> certificates;
		certificates = certificateDao.findCertificatesByPartOfName(nameContains);

		return certificates.stream().map(this::convertToDto).collect(Collectors.toList());
	}

	@Override
	public List<GiftCertificateGetDTO> getCertificatesByDescription(String description) {

		List<GiftCertificate> certificates;
		certificates = certificateDao.findCertificatesByDescription(description);

		return certificates.stream().map(this::convertToDto).collect(Collectors.toList());
	}

//	@Override
//	public List<GiftCertificateGetDTO> getCertificatesSorted(String sortBy, String sortDirection)
//			throws ServiceException {
//
//		List<GiftCertificate> certificates = new ArrayList<GiftCertificate>();
//		SearchParameters searchParameter = SearchParameters.getSearchParameters(sortBy);
//		try {
//			switch (searchParameter) {
//
//			case CREATION_DATE:
//				certificates = certificateDao.sortCertificatesByDate(sortDirection);
//				break;
//
//			case CERTIFICATE_NAME:
//				certificates = certificateDao.sortCertificatesByName(sortDirection);
//				break;
//
//			default:
//				certificates = certificateDao.sortCertificatesByName(sortDirection);
//			}
//
//		} catch (DaoException e) {
//			throw new ServiceException(
//					"Exception when calling getCertificatesByDateSorted() from CertificateServiceImpl", e);
//		}
//		return certificates.stream().map(this::convertToDto).collect(Collectors.toList());
//	}

	@Override
	public List<GiftCertificateGetDTO> sortCertificate(List<GiftCertificateGetDTO> certificates, String sortBy) {

		switch (sortBy) {
		case DATE_ASC:
			certificates = sortByCreationDateASC(certificates);
			break;

		case DATE_DESC:
			certificates = sortByCreationDateDESC(certificates);
			break;

		case NAME_ASC:
			certificates = sortByNameASC(certificates);
			break;

		case NAME_DESC:
			certificates = sortByNameDESC(certificates);
			break;

		default:
			return certificates;
		}
		return certificates;
	}

	private List<GiftCertificateGetDTO> sortByCreationDateASC(List<GiftCertificateGetDTO> certificates) {
		return certificates.stream().sorted(Comparator.comparing(GiftCertificateGetDTO::getCreationDate))
				.collect(Collectors.toList());
	}

	private List<GiftCertificateGetDTO> sortByCreationDateDESC(List<GiftCertificateGetDTO> certificates) {
		return certificates.stream().sorted(Comparator.comparing(GiftCertificateGetDTO::getCreationDate).reversed())
				.collect(Collectors.toList());
	}

	private List<GiftCertificateGetDTO> sortByNameDESC(List<GiftCertificateGetDTO> certificates) {
		return certificates.stream().sorted(Comparator.comparing(GiftCertificateGetDTO::getName).reversed())
				.collect(Collectors.toList());
	}

	private List<GiftCertificateGetDTO> sortByNameASC(List<GiftCertificateGetDTO> certificates) {
		return certificates.stream().sorted(Comparator.comparing(GiftCertificateGetDTO::getName))
				.collect(Collectors.toList());
	}

	private GiftCertificateGetDTO convertToDto(GiftCertificate giftCertificate) {

		if (giftCertificate.getTags() == null) {
			giftCertificate.setTags(Collections.emptyList());
		}

		GiftCertificateGetDTO certificateDTO = modelMapper.map(giftCertificate, GiftCertificateGetDTO.class);

		return certificateDTO;
	}

//	private GiftCertificateGetForCreationDTO convertToDtoForCreationOperation(GiftCertificate giftCertificate) {
//
//		if (giftCertificate.getTags() == null) {
//			giftCertificate.setTags(Collections.emptyList());
//		}
//
//		GiftCertificateGetForCreationDTO certificateDTO = modelMapper.map(giftCertificate,
//				GiftCertificateGetForCreationDTO.class);
//
//		return certificateDTO;
//	}

//	private GiftCertificateGetForUpdateDTO convertToDtoForUpdationOperation(GiftCertificate giftCertificate) {
//
//		if (giftCertificate.getTags() == null) {
//			giftCertificate.setTags(Collections.emptyList());
//		}
//
//		GiftCertificateGetForUpdateDTO certificateDTO = modelMapper.map(giftCertificate,
//				GiftCertificateGetForUpdateDTO.class);
//
//		return certificateDTO;
//	}

	private GiftCertificate convertToEntity(GiftCertificateCreateUpdateDTO giftDTO) {

		GiftCertificate certificate = modelMapper.map(giftDTO, GiftCertificate.class);

		return certificate;
	}

}
