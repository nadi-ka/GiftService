package com.epam.esm.service;

import java.util.List;

import com.epam.esm.entity.Tag;

public interface TagService {
	
	public List<Tag> getTags() throws ServiceException;

	public void saveTag(Tag theTag) throws ServiceException;

	public Tag getTag(long theId) throws ServiceException;

	public void deleteTag(long theId) throws ServiceException;

}
