package com.epam.esm.entity.search_param;

public enum SearchParameters {
	
	CREATION_DATE("creationDate"),
	CERTIFICATE_NAME("certName"),
	TAG_NAME("tagName"),
	PART_OF_CERT_NAME("partOfCertName"),
	CERT_DESCRIPTION("certDescription");
	
	private final String parameterValue;
	
	private SearchParameters(String theParamValue) {
		this.parameterValue = theParamValue;
	}
	
	public String getParameterValue() {
		return this.parameterValue;
	}
	
	public static SearchParameters getSearchParameters(String paramValue) {
		for(SearchParameters item: values()) {
			if (item.parameterValue.equals(paramValue)) {
				return item;
			}
		}
		return getDefault();
	}
	
	public static SearchParameters getDefault() {
		return SearchParameters.CERTIFICATE_NAME;
	}

}
