package com.toolrental.utils;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;

public class CustomRentalCalendarTests {

	@Test
	public void LaborDayTest() {
		//year 2022, Labor Day Monday September 5
		LocalDate laborDay2022 = LocalDate.of(2022, 9, 5);
		CustomRentalCalendar crc = new CustomRentalCalendar(2022);
		LocalDate laborDayFromCustomCalendar = crc.LaborDayObserved(2022);
		assertTrue(laborDay2022.equals(laborDayFromCustomCalendar));
		assertTrue(crc.getHolidays().contains(laborDayFromCustomCalendar));
	}
	
	@Test
	public void IndependenceDayObservanceTest() {
		//year 2020, Independence Day Saturday July 4
		LocalDate indepDay2020 = LocalDate.of(2020, 7, 4);
		CustomRentalCalendar crc = new CustomRentalCalendar(2020);
		LocalDate indepDayObserved = crc.IndependenceDayObserved(2020);
		assertTrue(indepDay2020.minusDays(1).equals(indepDayObserved));
		assertTrue(crc.getHolidays().contains(indepDayObserved));
		
		//year2021, Independence Day Sunday July 4
		indepDay2020 = LocalDate.of(2020, 7, 4);
		crc = new CustomRentalCalendar(2020);
		indepDayObserved = crc.IndependenceDayObserved(2020);
		assertTrue(indepDay2020.minusDays(1).equals(indepDayObserved));
		assertTrue(crc.getHolidays().contains(indepDayObserved));
	}
}
