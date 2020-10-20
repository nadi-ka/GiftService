package com.epam.esm.service;

import java.util.List;

import com.epam.esm.entity.Tag;

public interface TagService {
	
	public List<Tag> getTags();

	public void saveTag(Tag theTag);

	public Tag getTag(long theId);

	public void deleteTag(long theId);

}
