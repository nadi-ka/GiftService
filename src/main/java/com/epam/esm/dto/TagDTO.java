package com.epam.esm.dto;

import java.util.List;

import com.epam.esm.entity.GiftCertificate;

public class TagDTO {
	
	private long id;
	private String name;
	private List<GiftCertificate> certificates;
	
	public TagDTO() {}

	public TagDTO(long id, String name, List<GiftCertificate> certificates) {
		this.id = id;
		this.name = name;
		this.certificates = certificates;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<GiftCertificate> getCertificates() {
		return certificates;
	}

	public void setCertificates(List<GiftCertificate> certificates) {
		this.certificates = certificates;
	}

	@Override
	public String toString() {
		return "TagDTO [id=" + id + ", name=" + name + "]";
	}
	
}
