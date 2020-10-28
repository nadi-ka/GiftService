package com.epam.esm.service;

import java.util.List;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.service.exception.IllegalOperationServiceException;

public interface TagService {
	
	List<TagDTO> getTags();
	
	TagDTO getTag(long theId);

	TagDTO saveTag(TagDTO theTag);

	void updateTag(TagDTO theTag);

	void deleteTag(long theId) throws IllegalOperationServiceException;

}
