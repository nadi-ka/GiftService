package com.epam.esm.service;

import java.util.List;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.service.exception.ServiceException;

public interface TagService {
	
	List<TagDTO> getTags();
	
	TagDTO getTag(long theId);

	TagDTO saveTag(TagDTO theTag) throws ServiceException;

	void updateTag(TagDTO theTag) throws ServiceException;

	void deleteTag(long theId) throws ServiceException;

}
