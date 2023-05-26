package com.toolrental;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.toolrental.exceptions.ToolRentalException;

public class ScenarioTest {

	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();

	@Test
	public void Test1() throws ToolRentalException {
		// Tool code JAKR
		// Checkout date 9/3/15
		// Rental days 5
		// Discount 101%
		Tool testTool = new Tool("JAKR", "Jackhammer", "Ridgid", 2.99, true, false, false);
		int rentalDays = 5;
		int discount = 101;
		LocalDate checkoutDate = LocalDate.of(2015, 9, 3);
		Service service = new Service();

		int chargeDays = service.getChargeDays(testTool, rentalDays, checkoutDate);
		assertEquals(chargeDays, 2);

		double rentPreDiscount = service.calculateRentPreDiscount(testTool, chargeDays);
		assertTrue(rentPreDiscount == 2 * 2.99);
		exceptionRule.expect(ToolRentalException.class);
		exceptionRule.expectMessage("Rental discount must be between 0 and 100");
		service.calculateRentWithDiscount(rentPreDiscount, discount);
	}

	@Test
	public void Test2() throws ToolRentalException {
		// Tool code LADW
		// Checkout date 7/2/20
		// Rental days 3
		// Discount 10%
		Tool testTool = new Tool("LADW", "Ladder", "Werner", 1.99, true, true, false);
		int rentalDays = 3;
		int discount = 10;
		LocalDate checkoutDate = LocalDate.of(2020, 7, 2);
		Service service = new Service();

		int chargeDays = service.getChargeDays(testTool, rentalDays, checkoutDate);
		assertEquals(chargeDays, 2);

		double rentPreDiscount = service.calculateRentPreDiscount(testTool, chargeDays);
		assertTrue(rentPreDiscount == chargeDays * testTool.getDailyCharge());

		double rentWithDiscount = service.calculateRentWithDiscount(rentPreDiscount, discount);
		assertTrue(rentPreDiscount * (100 - discount) / 100 == rentWithDiscount);
	}

	@Test
	public void Test3() {
		// Tool code CHNS
		// Checkout date 7/2/15
		// Rental days 5
		// Discount 25%
		Tool testTool = new Tool("CHNS", "Chainsaw", "Stihl", 1.49, true, false, true);
		int rentalDays = 5;
		LocalDate checkoutDate = LocalDate.of(2015, 7, 2);
		Service service = new Service();
		int chargeDays = service.getChargeDays(testTool, rentalDays, checkoutDate);
		assertTrue(chargeDays == 3);
	}
}
