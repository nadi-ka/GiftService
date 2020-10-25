package com.epam.esm.rest;

import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epam.esm.dto.GiftCertificateCreateUpdateDTO;
import com.epam.esm.dto.GiftCertificateGetDTO;
import com.epam.esm.dto.GiftCertificateGetForCreationDTO;
import com.epam.esm.dto.GiftCertificateGetForUpdateDTO;
import com.epam.esm.rest.exception.NotFoundException;
import com.epam.esm.rest.exception.NotSavedException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.exception.ServiceException;

@RestController
@RequestMapping("/certificate-api")
public class CertificateController {

	@Autowired
	private CertificateService certificateService;

	private static final Logger log = LogManager.getLogger(CertificateController.class);

	/**
	 * GET method, which returns the List, which contains all certificates from the
	 * Database;
	 */
	@GetMapping("/certificates")
	public List<GiftCertificateGetDTO> getCertificates() {

		List<GiftCertificateGetDTO> certificates;
		try {
			certificates = certificateService.getCertificates();
		} catch (ServiceException e) {
			log.log(Level.ERROR,
					"Error when calling GetMapping command getCertificates() from the CertificateController", e);
			throw new NotFoundException("Nothing was found by the request", e);
		}
		return certificates;
	}

	/**
	 * GET certificate by the long Id; In case when the certificate with the given
	 * Id is not found, the method returns Status Code = 404
	 */
	@GetMapping("/certificates/{certificateId}")
	public GiftCertificateGetDTO getSertificate(@PathVariable long certificateId) {

		GiftCertificateGetDTO giftCertificate = certificateService.getCertificate(certificateId);

		if (giftCertificate == null) {
			throw new NotFoundException("The certificate wasn't found, id - " + certificateId);
		}
		return giftCertificate;
	}

	/**
	 * POST method which adds new certificate into the Database; In case of success,
	 * the method returns Status Code = 200 and the response body contains new
	 * generated certificates's Id; The method also bounds new certificate with
	 * appropriate tags in M2M table; The argument (GiftCertificateCreateUpdateDTO
	 * theCertificate) should contains the List of tags to bound the certificate
	 * with
	 */
	@PostMapping("/certificates")
	public GiftCertificateGetForCreationDTO addCertificate(@RequestBody GiftCertificateCreateUpdateDTO theCertificate) {

		GiftCertificateGetForCreationDTO certificateGetDTO;
		try {
			certificateGetDTO = certificateService.saveCertificate(theCertificate);
		} catch (ServiceException e) {
			log.log(Level.ERROR,
					"Error when calling PostMapping command addCertificate() from the CertificateController", e);
			throw new NotSavedException("The cartificate wasn't saved", e);
		}
		return certificateGetDTO;
	}

	/**
	 * PUT method which allows to change the certificate's Name, Description, Price,
	 * Duration and the tags to bound with; In case of success, the method returns
	 * Status Code = 200
	 */
	@PutMapping("/certificates")
	public GiftCertificateGetForUpdateDTO updateTag(@RequestBody GiftCertificateCreateUpdateDTO theCertificate) {

		GiftCertificateGetForUpdateDTO certificateDTO;
		try {
			certificateDTO = certificateService.updateCertificate(theCertificate);
		} catch (ServiceException e) {
			log.log(Level.ERROR,
					"Error when calling PutMapping command updateCertificate() from the CertificateController", e);
			throw new NotSavedException("The tag wasn't updated", e);
		}
		return certificateDTO;
	}

	/**
	 * DELETE certificate by long Id; In case when the certificate with the given Id is not found,
	 * the method returns Status Code = 200 OK 
	 */
	@DeleteMapping("/certificates/{certificateId}")
	public ResponseEntity<?> deleteCertificate(@PathVariable long certificateId) {

		GiftCertificateGetDTO certificate;
		try {
			certificate = certificateService.getCertificate(certificateId);
			if (certificate == null) {
				return ResponseEntity.status(HttpStatus.OK)
						.body("The certificate doesn't exist in base, id - " + certificateId);
			}

			// certificate was found and will be deleted;
			certificateService.deleteCertificate(certificateId);
		} catch (ServiceException e) {
			log.log(Level.ERROR,
					"Error when calling DaleteMapping command deleteCertificate() from the CertificateController", e);
		}
		return ResponseEntity.status(HttpStatus.OK)
				.body("The certificate was successfully deleted, id - " + certificateId);
	}

}
