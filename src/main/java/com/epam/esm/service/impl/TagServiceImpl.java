package com.epam.esm.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.esm.dal.TagDao;
import com.epam.esm.dal.exception.DaoException;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.IllegalOperationServiceException;
import com.epam.esm.service.exception.ServiceException;

@Service
public class TagServiceImpl implements TagService {

	private static final Logger log = LogManager.getLogger(TagServiceImpl.class);

	@Autowired
	private TagDao tagDao;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<TagDTO> getTags() {

		List<Tag> tags;
		tags = tagDao.findAllTags();

		return tags.stream().map(this::convertToDto).collect(Collectors.toList());
	}

	@Override
	public TagDTO saveTag(TagDTO theTag) throws ServiceException {

		Tag resultTag = null;

		try {
			resultTag = tagDao.addTag(convertToEntity(theTag));

		} catch (DaoException e) {
			throw new ServiceException("Exception when calling saveTag() from TagServiceImpl", e);
		}
		return convertToDto(resultTag);
	}

	@Override
	public TagDTO getTag(long theId) {

		Tag tag = tagDao.findTag(theId);

		if (tag == null) {
			return null;
		}
		return convertToDto(tag);
	}

	@Override
	public void updateTag(TagDTO theTag) throws ServiceException {

		int affectedRows = 0;
		try {
			affectedRows = tagDao.updateTag(convertToEntity(theTag));
		} catch (DaoException e) {
			throw new ServiceException("Exception when calling updateTag() from TagServiceImpl", e);
		}
		if (affectedRows == 0) {
			throw new ServiceException("The tag wasn't saved: " + theTag);
		}
	}

	@Override
	public void deleteTag(long theId) throws ServiceException {

		int affectedRows = 0;

		// check if there is at least one certificate, bounded with the tag for delete
		// operation;
		// if the method returns cartificateId - the operation will be forbidden;
		// if the method returns cartificateId=0, the tag could be deleted;
		long certificateId = tagDao.findCertificateIdByTagId(theId);

		if (certificateId != 0) {
			throw new IllegalOperationServiceException(
					"The tag is bounded with one or more certififcates and coudn't be deleted, tagId - " + theId);
		}
		try {
			affectedRows = tagDao.deleteTag(theId);
		} catch (DaoException e) {
			throw new ServiceException("Exception when calling deleteTag() from TagServiceImpl", e);
		}
		if (affectedRows == 0) {
			throw new ServiceException("The tag wasn't deleted, tagId - " + theId);
		}
	}

	private TagDTO convertToDto(Tag tag) {

		TagDTO tagDTO = modelMapper.map(tag, TagDTO.class);

		return tagDTO;
	}

	private Tag convertToEntity(TagDTO tagDTO) {

		Tag tag = modelMapper.map(tagDTO, Tag.class);

		return tag;
	}

}
