package com.epam.esm.dal;

import java.util.List;

import com.epam.esm.dal.exception.DaoException;
import com.epam.esm.entity.Tag;

public interface TagDao {
	
	Tag addTag(Tag tag) throws DaoException;
	
	int updateTag(Tag tag) throws DaoException;
	
	List<Tag> findAllTags() throws DaoException;
	
	Tag findTag(long id);

	int deleteTag(long id) throws DaoException;
	
	long findCertificateIdByTagId(long tagId);

}
