package com.epam.esm.rest;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.epam.esm.dto.GiftCertificateCreateUpdateDTO;
import com.epam.esm.dto.GiftCertificateGetDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.rest.exception.InvalidRequestParametersException;
import com.epam.esm.rest.exception.NotFoundException;
import com.epam.esm.rest.parameter.FilterParam;
import com.epam.esm.rest.parameter.OrderParam;
import com.epam.esm.rest.parameter.ParameterConstant;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.TagService;

@RestController
@RequestMapping("/certificate-api")
public class CertificateController {

	@Autowired
	private CertificateService certificateService;

	@Autowired
	private TagService tagService;

	private static final Logger log = LogManager.getLogger(CertificateController.class);

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
	public GiftCertificateGetDTO addCertificate(@Valid @RequestBody GiftCertificateCreateUpdateDTO theCertificate) {

		// the certificate can't exists without tag
		if (theCertificate.getTags() == null || theCertificate.getTags().isEmpty()) {
			throw new InvalidRequestParametersException("The certificate should be bounded at least with one tag");
		}
		// Check if bounded with certificate tags exist
		for (TagDTO tag : theCertificate.getTags()) {
			TagDTO tagToBoundWithCertificate = tagService.getTag(tag.getId());

			// If the tag with given id wasn't found - the operation can't be performed;
			if (tagToBoundWithCertificate == null) {
				throw new NotFoundException(
						"The tag, should be bounded with the certificate, wasn't found, tag id - " + tag.getId());
			}
		}

		GiftCertificateGetDTO certificateGetDTO = certificateService.saveCertificate(theCertificate);

		return certificateGetDTO;
	}

	/**
	 * PUT method which allows to change the certificate's Name, Description, Price,
	 * Duration and the tags to bound with; In case of success, the method returns
	 * Status Code = 200
	 */
	@PutMapping("/certificates")
	public GiftCertificateGetDTO updateCertificate(@Valid @RequestBody GiftCertificateCreateUpdateDTO theCertificate) {

		GiftCertificateGetDTO certificateDTO = certificateService.updateCertificate(theCertificate);
		if (certificateDTO == null) {
			throw new NotFoundException("The certificate with given Id wasn't found;");
		}

		return certificateDTO;
	}

	/**
	 * DELETE certificate by long Id; In case when the certificate with the given Id
	 * is not found, the method returns Status Code = 200 OK
	 */
	@DeleteMapping("/certificates/{certificateId}")
	public ResponseEntity<?> deleteCertificate(@PathVariable long certificateId) {

		// check if the certificate with given Id exists;
		GiftCertificateGetDTO certificate = certificateService.getCertificate(certificateId);
		if (certificate == null) {
			return ResponseEntity.status(HttpStatus.OK)
					.body("The certificate doesn't exist in base, id - " + certificateId);
		}

		// certificate was found and will be deleted;
		certificateService.deleteCertificate(certificateId);

		return ResponseEntity.status(HttpStatus.OK)
				.body("The certificate was successfully deleted, id - " + certificateId);
	}

	/**
	 * GET method, which returns the List of GiftCertificates; accepts optional
	 * parameters tag, name, description, order, direction; parameters could be used
	 * in conjunction;
	 */
	@GetMapping("/certificates")
	public @ResponseBody List<GiftCertificateGetDTO> getCertificates(@RequestParam(required = false) String tag,
			@RequestParam(required = false) String name, @RequestParam(required = false) String description,
			@RequestParam(required = false, defaultValue = "name") @ModelAttribute("orderParam") String order,
			@RequestParam(required = false, defaultValue = "desc") @Valid @ModelAttribute("orderParam") String direction) {

		List<GiftCertificateGetDTO> certificates = new ArrayList<GiftCertificateGetDTO>();

		List<FilterParam> filterParams = new ArrayList<FilterParam>();
		List<OrderParam> orderParams = new ArrayList<OrderParam>();

		if (tag != null && !tag.isEmpty()) {
			filterParams.add(new FilterParam(ParameterConstant.TAG, tag));
		}
		if (name != null && !name.isEmpty()) {
			filterParams.add(new FilterParam(ParameterConstant.CERTIFICATE_NAME, name));
		}
		if (description != null && !description.isEmpty()) {
			filterParams.add(new FilterParam(ParameterConstant.DESCRIPTION, description));
		}

		orderParams.add(new OrderParam(order, direction));

		certificates = certificateService.getCertificates(filterParams, orderParams);

		// if List is empty - exception is handled as 404NotFound;
		if (certificates.isEmpty()) {
			throw new NotFoundException("Nothing was found by the request");
		}
		return certificates;
	}

}
