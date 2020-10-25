package com.epam.esm.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class GiftCertificateGetForUpdateDTO extends GiftCertificateCreateUpdateDTO{

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
	private LocalDateTime lastUpdateDate;

	public LocalDateTime getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	@Override
	public String toString() {
		return "GiftCertificateGetForUpdateDTO [lastUpdateDate=" + lastUpdateDate + ", getId()=" + getId()
				+ ", getName()=" + getName() + ", getDescription()=" + getDescription() + ", getPrice()=" + getPrice()
				+ ", getDuration()=" + getDuration() + ", getTags()=" + getTags() + "]";
	}
	
	

}
