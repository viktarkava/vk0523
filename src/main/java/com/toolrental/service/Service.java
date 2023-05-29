package com.toolrental.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.toolrental.entity.RentalAgreement;
import com.toolrental.entity.Tool;
import com.toolrental.exceptions.ToolRentalException;
import com.toolrental.utils.CustomRentalCalendar;

//vk0523

public class Service {
	List<Tool> tools;

	public Service() {
		this.tools = new ArrayList<>();
	}

	public void addTool(Tool tool) {
		this.tools.add(tool);
	}

	public List<Tool> getTools() {
		return tools;
	}

	public void setTools(List<Tool> tools) {
		this.tools = tools;
	}

	public Tool getTool(Scanner sc) throws ToolRentalException {
		String code;
		Tool tool = null;
		while (tool == null) {
			System.out.println("Enter Tool code - ");
			code = sc.nextLine();
			tool = getToolFromDB(code);
		}
		return tool;
	}

	private Tool getToolFromDB(String code) throws ToolRentalException {
		Tool ret = null;
		for (Tool t : tools) {
			if (t.getCode().equals(code.toUpperCase())) {
				ret = t;
			}
		}
		if (ret == null) {
			throw new ToolRentalException("Tool with code " + code + " is not available. Please try again.");
		}
		return ret;
	}

	public int getRentalDayCount(Scanner sc) throws ToolRentalException {
		int rentalDays = -1;
		while (rentalDays == -1) {
			System.out.println("Enter number or rental days - ");
			try {
				rentalDays = sc.nextInt();
			} catch (InputMismatchException ime) {
				System.err.println("please enter valid number of days");
				sc.nextLine();
			}
			verifyDaysOfRental(rentalDays);
		}
		return rentalDays;
	}

	public int getDiscount(Scanner sc) throws ToolRentalException {
		int discount = -1;
		System.out.println("Enter discount - ");
		try {
			discount = sc.nextInt();
		} catch (InputMismatchException ime) {
			System.err.println("Discount must be a number in the range 0 - 100");
			sc.nextLine();
		}
		verifyDiscount(discount);
		return discount;
	}

	public void verifyDiscount(int discount) throws ToolRentalException {
		if (discount > 100 || discount < 0) {
			throw new ToolRentalException("Rental discount must be between 0 and 100");
		}
	}

	public void verifyDaysOfRental(int daysOfRental) throws ToolRentalException {
		if (daysOfRental < 1) {
			throw new ToolRentalException("Rental day count must be 1 or greater.");
		}
	}

	public LocalDate getCheckoutDate(Scanner sc) throws ToolRentalException {
		String date;
		Date ret = null;
		while (ret == null) {
			System.out.println("Enter Check out date - ");
			date = sc.nextLine();
			SimpleDateFormat fromUser = new SimpleDateFormat("MM/dd/yyyy");
			fromUser.setLenient(false);
			try {
				ret = fromUser.parse(date);
			} catch (ParseException e) {
				System.err.println("Enter correct date MM/dd/yyyy");
				ret = null;
			}
		}
		return ret.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public int getChargeDays(Tool tool, int daysOfRental, LocalDate startDate) {
		int ret = 0;
		// Charge days - Count of chargeable days, from day after checkout through and
		// including due date, excluding “no charge” days as specified by the tool type.
		CustomRentalCalendar cc = new CustomRentalCalendar(startDate.getYear());
		startDate = startDate.plusDays(1);
		LocalDate endDate = startDate.plusDays(daysOfRental - 1);
		if (tool.isWeekdayCharge()) {
			int numberOfWorkDays = cc.getNumberOfWorkdaysBetween(startDate, endDate);
			ret += numberOfWorkDays;
		}
		if (tool.isWeekendCharge()) {
			int numberOfWeekends = cc.getNumberOfWeekendsBetween(startDate, endDate);
			ret += numberOfWeekends;
		}
		if (tool.isHolidayCharge()) {
			int numberOfHolidays = cc.getNumberOfHolidaysBetween(startDate, endDate);
			ret += numberOfHolidays;
		}
		return ret;
	}

	public double calculateRentPreDiscount(Tool tool, int chargeDays) throws ToolRentalException {
		return chargeDays * tool.getDailyCharge();
	}

	public RentalAgreement generateAgreement(Tool tool, int rentalDays, int discount, LocalDate date)
			throws ToolRentalException {

		verifyDiscount(discount);
		verifyDaysOfRental(rentalDays);
		tool = getToolFromDB(tool.getCode());
		RentalAgreement agreement = new RentalAgreement(tool);
		agreement.setToolCode(tool.getCode());
		agreement.setToolType(tool.getType());
		agreement.setToolBrand(tool.getBrand());
		agreement.setRentalDays(rentalDays);
		agreement.setCheckoutDate(date.toString());
		agreement.setDueDate(date.plusDays(rentalDays).toString());
		agreement.setDailyRentalCharge(tool.getDailyCharge());
		int chargeDays = getChargeDays(tool, rentalDays, date);
		agreement.setChargeDays(chargeDays);
		double rentPreDiscount = calculateRentPreDiscount(tool, chargeDays);
		agreement.setPreDiscountCharge(roundUp(rentPreDiscount));
		agreement.setDiscountPercent(discount);
		double discountAmount = rentPreDiscount * discount / 100;
		agreement.setDiscountAmount(roundUp(discountAmount));
		agreement.setFinalCharge(roundUp(rentPreDiscount - discountAmount));

		return agreement;
	}

	public double roundUp(double in) {
		return Math.round(in * 100.0) / 100.0;
	}
}
