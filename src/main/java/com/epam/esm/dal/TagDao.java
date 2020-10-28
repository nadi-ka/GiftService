package com.epam.esm.dal;

import java.util.List;

import com.epam.esm.entity.Tag;

public interface TagDao {
	
	Tag addTag(Tag tag);
	
	int updateTag(Tag tag);
	
	List<Tag> findAllTags();
	
	Tag findTag(long id);

	int deleteTag(long id);
	
	long findCertificateIdByTagId(long tagId);

}
