package com.toolrental;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.toolrental.entity.RentalAgreement;
import com.toolrental.entity.Tool;
import com.toolrental.exceptions.ToolRentalException;
import com.toolrental.service.Service;

//vk0523

public class ServiceTests {

	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();

	@Test
	public void verifyDiscountTest() throws ToolRentalException {
		Service s = new Service();
		int discount = 101;
		exceptionRule.expect(ToolRentalException.class);
		exceptionRule.expectMessage("Rental discount must be between 0 and 100");
		s.verifyDiscount(discount);
	}

	@Test
	public void verifyDaysOfRentalTest() throws ToolRentalException {
		Service s = new Service();
		int daysOfRental = -1;
		exceptionRule.expect(ToolRentalException.class);
		exceptionRule.expectMessage("Rental day count must be 1 or greater.");
		s.verifyDaysOfRental(daysOfRental);
	}

	@Test
	public void Test1() throws ToolRentalException {
		// Tool code JAKR
		// Checkout date 9/3/15
		// Rental days 5
		// Discount 101%
		Tool testTool = new Tool("JAKR", "Jackhammer", "Ridgid", 2.99, true, false, false);
		int rentalDays = 5;
		int discountPercent = 101;
		LocalDate checkoutDate = LocalDate.of(2015, 9, 3);
		exceptionRule.expect(ToolRentalException.class);
		exceptionRule.expectMessage("Rental discount must be between 0 and 100");
		assertValues(testTool, rentalDays, discountPercent, checkoutDate, 2);
	}

	@Test
	public void Test2() throws ToolRentalException {
		// Tool code LADW
		// Checkout date 7/2/20
		// Rental days 3
		// Discount 10%
		Tool testTool = new Tool("LADW", "Ladder", "Werner", 1.99, true, true, false);
		int rentalDays = 3;
		LocalDate checkoutDate = LocalDate.of(2020, 7, 2);
		int discountPercent = 10;
		
		// 7/2/2020 - Th Checkout day. No charge.
		// 7/3/2020 - Fr July 4th observed. Holiday. No charge.
		// 7/4/2020 - Sa Weekend. Charge day.
		// 7/5/2020 - Su Weekend. Charge day.

		int chargeDays = 2;
		assertValues(testTool, rentalDays, discountPercent, checkoutDate, chargeDays);
	}

	@Test
	public void Test3() throws ToolRentalException {
		// Tool code CHNS
		// Checkout date 7/2/2015
		// Rental days 5
		// Discount 25%

		Tool testTool = new Tool("CHNS", "Chainsaw", "Stihl", 1.49, true, false, true);
		int rentalDays = 5;
		LocalDate checkoutDate = LocalDate.of(2015, 7, 2);
		int discountPercent = 25;

		// 7/2/2015 - Th Checkout day. No charge.
		// 7/3/2015 - Fr July 4th observed. Holiday. Charge day.
		// 7/4/2015 - Sa Weekend. No charge.
		// 7/5/2015 - Su Weekend. No charge.
		// 7/6/2015 - Mo Charge day.
		// 7/7/2015 - Tu Charge day. Due Date.

		int chargeDays = 3;
		assertValues(testTool, rentalDays, discountPercent, checkoutDate, chargeDays);
	}
	
	@Test
	public void Test4() throws ToolRentalException {
		// Tool code JAKD
		// Checkout date 9/3/2015
		// Rental days 6
		// Discount 0%

		Tool testTool = new Tool("JAKD", "Jackhammer", "DeWalt", 2.99, true, false, false);
		int rentalDays = 6;
		LocalDate checkoutDate = LocalDate.of(2015, 9, 3);
		int discountPercent = 0;

		// 7/2/2015 - Th Checkout day. No charge.
		// 7/3/2015 - Fr July 4th observed. Holiday. Charge day.
		// 7/4/2015 - Sa Weekend. No charge.
		// 7/5/2015 - Su Weekend. No charge.
		// 7/6/2015 - Mo Charge day.
		// 7/7/2015 - Tu Charge day. Due Date.

		int chargeDays = 3;
		assertValues(testTool, rentalDays, discountPercent, checkoutDate, chargeDays);
	}

	private void assertValues(Tool testTool, int rentalDays, int discountPercent, LocalDate checkoutDate,
			int chargeDays) throws ToolRentalException {
		Service service = new Service();
		RentalAgreement agreement = service.generateAgreement(testTool, rentalDays, discountPercent, checkoutDate);
		assertTrue(testTool.getCode().equals(agreement.getToolCode()));
		assertTrue(testTool.getBrand().equals(agreement.getToolBrand()));
		assertTrue(testTool.getType().equals(agreement.getToolType()));
		assertTrue(rentalDays == agreement.getRentalDays());
		assertTrue(checkoutDate.toString().equals(agreement.getCheckoutDate()));
		assertTrue(checkoutDate.plusDays(rentalDays).toString().equals(agreement.getDueDate()));
		assertTrue(testTool.getDailyCharge() == agreement.getDailyRentalCharge());
		assertTrue(chargeDays == agreement.getChargeDays());
		double rentPreDiscount = service.calculateRentPreDiscount(testTool, chargeDays);
		assertTrue(rentPreDiscount == agreement.getPreDiscountCharge());
		assertTrue(discountPercent == agreement.getDiscountPercent());
		double discountAmount = rentPreDiscount * discountPercent / 100;
		assertTrue(discountAmount == agreement.getDiscountAmount());
		double finalCharge = rentPreDiscount * (100 - discountPercent) / 100;
		assertTrue(finalCharge == agreement.getFinalCharge());
	}
}
