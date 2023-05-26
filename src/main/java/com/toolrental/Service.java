package com.toolrental;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.toolrental.exceptions.ToolRentalException;

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

	public void verifyDaysOfRental(int daysOfRental) throws ToolRentalException {
		if (daysOfRental < 1) {
			throw new ToolRentalException("Rental day count must be 1 or greater.");
		}
	}

	public Tool getTool(Scanner sc) throws ToolRentalException {
		String code;
		Tool ret = null;
		while (ret == null) {
			System.out.println("Enter Tool code - ");
			code = sc.nextLine();
			for (Tool t : tools) {
				if (t.getCode().equals(code.toUpperCase())) {
					ret = t;
				}
			}
			if (ret == null) {
				throw new ToolRentalException("Tool with code " + code + " is not available. Please try again.");
			}
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
		CustomRentalCalendar cc = new CustomRentalCalendar(startDate.getYear());
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

	double calculateRentPreDiscount(Tool tool, int chargeDays) throws ToolRentalException {
		return chargeDays * tool.getDailyCharge();
	}

	double calculateRentWithDiscount(double rentPreDiscount, int discount) throws ToolRentalException {
		verifyDiscount(discount);
		return rentPreDiscount * (100 - discount) / 100;
	}

}
