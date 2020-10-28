package com.epam.esm.rest.parameter;

import javax.validation.constraints.Pattern;

public class OrderParam {

	@Pattern(regexp = "^date|name$")
	private String name;
	
	@Pattern(regexp = "^asc|desc$")
	private String direction;
	
	public OrderParam() {}

	public OrderParam(String name, String direction) {
		this.name = name;
		this.direction = direction;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	@Override
	public String toString() {
		return "OrderParam [name=" + name + ", direction=" + direction + "]";
	}
	
}
