package com.epam.esm.dto;

import java.util.List;


public class GiftCertificateCreateUpdateDTO {
	
	private long id;
	private String name;
	private String description;
	private double price;
	private int duration;
	private List<TagDTO> tags;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public List<TagDTO> getTags() {
		return tags;
	}

	public void setTags(List<TagDTO> tags) {
		this.tags = tags;
	}

	@Override
	public String toString() {
		return "GiftCertificateCreateDTO [id=" + id + ", name=" + name + ", description=" + description + ", price="
				+ price + ", duration=" + duration + ", tags=" + tags + "]";
	}

}
