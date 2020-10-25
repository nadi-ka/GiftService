package com.epam.esm.service.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeFormatterISO {
	
	private static final String formatPattern = "yyyy-MM-dd HH:mm:ss";
	
	public static LocalDateTime createAndformatDateTime() {
	
	    ZonedDateTime zoneEuropeMinsk = ZonedDateTime.now(ZoneId.of("Europe/Minsk"));

	    LocalDateTime ldt = zoneEuropeMinsk.toLocalDateTime();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern);
	    zoneEuropeMinsk.format(formatter);
	    
	    return ldt;
	
	}

}
