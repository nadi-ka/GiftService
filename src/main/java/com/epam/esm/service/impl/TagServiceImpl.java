package com.epam.esm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.esm.dal.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;

@Service
public class TagServiceImpl implements TagService {
	
	@Autowired
	private TagDao tagDao;

	@Override
	public List<Tag> getTags() {
		return tagDao.findAllTags();
	}

	@Override
	public void saveTag(Tag theTag) {
		// TODO Auto-generated method stub

	}

	@Override
	public Tag getTag(long theId) {
		
		return tagDao.findTag(theId);
	}

	@Override
	public void deleteTag(long theId) {
		// TODO Auto-generated method stub

	}

}
