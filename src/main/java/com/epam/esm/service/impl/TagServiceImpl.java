package com.epam.esm.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.esm.dal.TagDao;
import com.epam.esm.dal.exception.DaoException;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.ServiceException;
import com.epam.esm.service.TagService;

@Service
public class TagServiceImpl implements TagService {

	private static final Logger log = LogManager.getLogger(TagServiceImpl.class);

	@Autowired
	private TagDao tagDao;

	@Override
	public List<Tag> getTags() throws ServiceException {

		List<Tag> tags;
		try {
			tags = tagDao.findAllTags();
		} catch (DaoException e) {
			throw new ServiceException("Exception when calling getTags() from TagServiceImpl", e);
		}
		return tags;
	}

	@Override
	public void saveTag(Tag theTag) throws ServiceException {
		int updatedRows = 0;
		try {
			updatedRows = tagDao.addTag(theTag);
		} catch (DaoException e) {
			throw new ServiceException("Exception when calling saveTag() from TagServiceImpl", e);
		}
		if (updatedRows == 0) {
			throw new ServiceException("The tag wasn't saved: " + theTag);
		}

	}

	@Override
	public Tag getTag(long theId) throws ServiceException {

		Tag tag;
		try {
			tag = tagDao.findTag(theId);
		} catch (DaoException e) {
			throw new ServiceException("Exception when calling sgetTag() from TagServiceImpl", e);
		}
		return tag;
	}

	@Override
	public void updateTag(Tag theTag) throws ServiceException {

		int affectedRows = 0;
		try {
			affectedRows = tagDao.updateTag(theTag);
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
		try {
			affectedRows = tagDao.deleteTag(theId);
		} catch (DaoException e) {
			throw new ServiceException("Exception when calling deleteTag() from TagServiceImpl", e);
		}
		if (affectedRows == 0) {
			throw new ServiceException("The tag wasn't deleted, tagId - " + theId);
		}
	}

}
