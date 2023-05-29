package com.toolrental;

import static org.junit.Assert.assertTrue;

import java.text.ParseException;
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

	private final Tool CHNS = new Tool("CHNS", "Chainsaw", "Stihl", 1.49, true, false, true);
	private final Tool JAKR = new Tool("JAKR", "Jackhammer", "Ridgid", 2.99, true, false, false);
	private final Tool LADW = new Tool("LADW", "Ladder", "Werner", 1.99, true, true, false);
	private final Tool JAKD = new Tool("JAKD", "Jackhammer", "DeWalt", 2.99, true, false, false);

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
	public void verifyNonExistentTool() throws ToolRentalException {
		Tool testTool = new Tool("AAA", "Jackhammer", "Ridgid", 2.99, true, false, false);
		int rentalDays = 5;
		int discountPercent = 10;
		LocalDate checkoutDate = LocalDate.of(2015, 9, 3);
		exceptionRule.expect(ToolRentalException.class);
		exceptionRule.expectMessage("Tool with code " + testTool.getCode() + " is not available. Please try again.");
		assertValues(testTool, rentalDays, discountPercent, checkoutDate, 2, false);
	}

	@Test
	public void Test1() throws ToolRentalException {
		// Tool code JAKR
		// Checkout date 9/3/15
		// Rental days 5
		// Discount 101%

		int rentalDays = 5;
		int discountPercent = 101;
		LocalDate checkoutDate = LocalDate.of(2015, 9, 3);
		exceptionRule.expect(ToolRentalException.class);
		exceptionRule.expectMessage("Rental discount must be between 0 and 100");
		assertValues(JAKR, rentalDays, discountPercent, checkoutDate, 2, true);
	}

	@Test
	public void Test2() throws ToolRentalException {
		// Tool code LADW
		// Checkout date 7/2/20
		// Rental days 3
		// Discount 10%

		int rentalDays = 3;
		LocalDate checkoutDate = LocalDate.of(2020, 7, 2);
		int discountPercent = 10;

		// 7/2/2020 - Th Checkout day. No charge.
		// 7/3/2020 - Fr July 4th observed. Holiday. No charge.
		// 7/4/2020 - Sa Weekend. Charge day.
		// 7/5/2020 - Su Weekend. Charge day.

		int chargeDays = 2;
		assertValues(LADW, rentalDays, discountPercent, checkoutDate, chargeDays, true);
	}

	@Test
	public void Test3() throws ToolRentalException {
		// Tool code CHNS
		// Checkout date 7/2/2015
		// Rental days 5
		// Discount 25%

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
		assertValues(CHNS, rentalDays, discountPercent, checkoutDate, chargeDays, true);
	}

	@Test
	public void Test4() throws ToolRentalException {
		// Tool code JAKD
		// Checkout date 9/3/2015
		// Rental days 6
		// Discount 0%

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
		assertValues(JAKD, rentalDays, discountPercent, checkoutDate, chargeDays, true);
	}

	@Test
	public void Test5() throws ToolRentalException {
		// Tool code JAKR
		// Checkout date 7/2/2015
		// Rental days 9
		// Discount 0%

		int rentalDays = 9;
		LocalDate checkoutDate = LocalDate.of(2015, 7, 2);
		int discountPercent = 0;

		// 7/2/2015 - Th Checkout day. No charge.
		// 7/3/2015 - Fr July 4th observed. Holiday. No charge.
		// 7/4/2015 - Sa Weekend. No charge.
		// 7/5/2015 - Su Weekend. No charge.
		// 7/6/2015 - Mo Charge day.
		// 7/7/2015 - Tu Charge day.
		// 7/8/2015 - We Charge day.
		// 7/9/2015 - Th Charge day.
		// 7/10/2015 - Fr Charge day.
		// 7/11/2015 - Sa No charge. Due Date

		int chargeDays = 5;
		assertValues(JAKR, rentalDays, discountPercent, checkoutDate, chargeDays, true);
	}
	
	@Test
	public void Test6() throws ToolRentalException, ParseException {
		// Tool code JAKR
		// Checkout date 7/2/2020
		// Rental days 4
		// Discount 50%

		int rentalDays = 4;
		LocalDate checkoutDate = LocalDate.of(2020, 7, 2);
		int discountPercent = 50;
		

		// 7/2/2020 - Th Checkout day. No charge.
		// 7/3/2020 - Fr July 4th observed. Holiday. No charge.
		// 7/4/2020 - Sa Weekend. No charge.
		// 7/5/2020 - Su Weekend. No charge.
		// 7/5/2020 - Mo Charge day. Due Date.

		int chargeDays = 1;
		assertValues(JAKR, rentalDays, discountPercent, checkoutDate, chargeDays, true);
	}

	private void assertValues(Tool testTool, int rentalDays, int discountPercent, LocalDate checkoutDate,
			int chargeDays, boolean addTools) throws ToolRentalException {
		Service service = new Service();
		if (addTools) {
			service.addTool(CHNS);
			service.addTool(JAKR);
			service.addTool(LADW);
			service.addTool(JAKD);
		}
		RentalAgreement agreement = service.generateAgreement(testTool, rentalDays, discountPercent, checkoutDate);
		assertTrue(testTool.getCode().equals(agreement.getToolCode()));
		assertTrue(testTool.getBrand().equals(agreement.getToolBrand()));
		assertTrue(testTool.getType().equals(agreement.getToolType()));
		assertTrue(rentalDays == agreement.getRentalDays());
		assertTrue(checkoutDate.toString().equals(agreement.getCheckoutDate()));
		assertTrue(checkoutDate.plusDays(rentalDays).toString().equals(agreement.getDueDate()));
		assertTrue(testTool.getDailyCharge() == agreement.getDailyRentalCharge());
		assertTrue(chargeDays == agreement.getChargeDays());
		double rentPreDiscount = service.roundUp(service.calculateRentPreDiscount(testTool, chargeDays));
		assertTrue(rentPreDiscount == agreement.getPreDiscountCharge());
		assertTrue(discountPercent == agreement.getDiscountPercent());
		double discountAmount = service.roundUp(rentPreDiscount * discountPercent / 100);
		assertTrue(discountAmount == agreement.getDiscountAmount());
		double finalCharge = service.roundUp(rentPreDiscount * (100 - discountPercent) / 100);
		assertTrue(finalCharge == agreement.getFinalCharge());
	}
}
