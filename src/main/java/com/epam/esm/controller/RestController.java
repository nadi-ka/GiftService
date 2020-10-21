package com.epam.esm.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.epam.esm.controller.exception.NotFoundException;
import com.epam.esm.controller.exception.NotSavedException;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.ServiceException;
import com.epam.esm.service.TagService;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RestController {

	private static final Logger log = LogManager.getLogger(RestController.class);

	@Autowired
	private TagService tagService;

	@GetMapping("/tags")
	public List<Tag> getTags() {

		List<Tag> tags = new ArrayList<Tag>();
		try {
			tags = tagService.getTags();
		} catch (ServiceException e) {
			log.log(Level.ERROR, "Error when calling GetMapping comand getTags() from the RestController", e);
			throw new NotFoundException("Nothing was found by the request", e);
		}
		return tags;
	}

	@GetMapping("/tags/{tagId}")
	public Tag getTag(@PathVariable long tagId) {

		Tag theTag;
		try {
			theTag = tagService.getTag(tagId);
		}catch (ServiceException e) {
			log.log(Level.ERROR, "Error when calling GetMapping comand getTag() from the RestController", e);
			throw new NotFoundException("The tag wasn't found", e);
		}
		return theTag;
	}

	@PostMapping("/tags")
	public void addTag(@RequestBody Tag theTag) {
		
		try {
		tagService.saveTag(theTag);
		}catch (ServiceException e) {
			log.log(Level.ERROR, "Error when calling PostMapping comand addTag() from the RestController", e);
			throw new NotSavedException("The tag wasn't saved", e);
		}

	}

}
