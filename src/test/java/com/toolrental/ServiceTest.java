package com.toolrental;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.toolrental.exceptions.ToolRentalException;

public class ServiceTest {
	
	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();
	
	@Test
	public void verifyDiscountTest() throws ToolRentalException {
		Service s = new Service();
		int discount = -1;
		exceptionRule.expect(ToolRentalException.class);
		exceptionRule.expectMessage("Rental day count must be 1 or greater.");
		s.verifyDaysOfRental(discount);
	}
}
