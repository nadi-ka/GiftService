package com.epam.esm.dal;

import java.util.List;

import com.epam.esm.entity.Tag;

public interface TagDao {
	
	int addTag(String tagName);
	
	void updateTag(long id, String tagName);
	
	List<Tag> findAllTags();
	
	Tag findTag(long id);

	void deleteTag(long id);

}
