package com.epam.esm.dal;

import java.util.List;

import com.epam.esm.dal.exception.DaoException;
import com.epam.esm.entity.Tag;

public interface TagDao {
	
	int addTag(Tag tag) throws DaoException;
	
	void updateTag(long id, String tagName) throws DaoException;
	
	List<Tag> findAllTags() throws DaoException;
	
	Tag findTag(long id) throws DaoException;

	void deleteTag(long id) throws DaoException;

}
