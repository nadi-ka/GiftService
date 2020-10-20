package com.epam.esm.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.epam.esm.dal.TagDao;
import com.epam.esm.dal.exception.DaoException;
import com.epam.esm.entity.Tag;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RestController {

	private List<Tag> theTags;
	
	@Autowired
	private TagDao tagDao;

	@PostConstruct
	public void loadData() {
		theTags = new ArrayList<>();

		theTags.add(new Tag(1L, "Sport"));
		theTags.add(new Tag(1L, "Leisure"));
		theTags.add(new Tag(1L, "SPA"));
	}

	@GetMapping("/tags")
	public List<Tag> getTags() throws DaoException{
		List<Tag> tags = tagDao.findAllTags();
		return tags;
	}

	@GetMapping("/tags/{tagId}")
	public Tag getTag(@PathVariable int tagId) {
		return theTags.get(tagId);
	}

}
