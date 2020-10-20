package com.epam.esm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.epam.esm.dal.exception.DaoException;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RestController {
	
	@Autowired
	private TagService tagService;

	@GetMapping("/tags")
	public List<Tag> getTags() throws DaoException{
		List<Tag> tags = tagService.getTags();
		return tags;
	}

	@GetMapping("/tags/{tagId}")
	public Tag getTag(@PathVariable long tagId) {
		return tagService.getTag(tagId);
	}

}
