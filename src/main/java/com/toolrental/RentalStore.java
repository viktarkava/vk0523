package com.toolrental;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Scanner;

import com.toolrental.exceptions.ToolRentalException;

public class RentalStore {
	public static void main(String[] args) throws Exception {
		Service service = new Service();
		service.addTool(new Tool("CHNS", "Chainsaw", "Stihl", 1.49, true, false, true));
		service.addTool(new Tool("LADW", "Ladder", "Werner", 1.99, true, true, false));
		service.addTool(new Tool("JAKD", "Jackhammer", "DeWalt", 2.99, true, false, false));
		service.addTool(new Tool("JAKR", "Jackhammer", "Ridgid", 2.99, true, false, false));

		Tool tool = null;
		int rentalDays = -1;
		int discount = -1;
		LocalDate date = null;

		try (Scanner sc = new Scanner(System.in)) {
			while (tool == null || rentalDays == -1 || discount == -1 || date == null) {
				try {
					if (tool == null) {
						tool = service.getTool(sc);
					}
					if (rentalDays == -1) {
						rentalDays = service.getRentalDayCount(sc);
					}
					sc.nextLine();
					if (discount == -1) {
						discount = service.getDiscount(sc);
					}
					sc.nextLine();
					date = service.getCheckoutDate(sc);
				} catch (ToolRentalException tre) {
					System.err.println(tre.getMessage());
				}
			}
		}
		System.out.println("Tool code: " + tool.getCode());
		System.out.println("Tool type: " + tool.getType());
		System.out.println("Tool Brand: " + tool.getBrand());
		System.out.println("Rental days: " + rentalDays);
		System.out.println("Check out date: " + date.toString());
		System.out.println("Due date: " + date.plusDays(rentalDays).toString());
		System.out.println("Daily rental charge: " + tool.getDailyCharge());
		int chargeDays = service.getChargeDays(tool, rentalDays, date);
		System.out.println("Charge days: " + chargeDays);
		double rentPreDiscount = 0;
		rentPreDiscount = service.calculateRentPreDiscount(tool, chargeDays);
		DecimalFormat df = new DecimalFormat("##,##0.00");
		System.out.println("Pre-discount charge: $" + df.format(rentPreDiscount));
		System.out.println("Discount percent: " + discount + "%");
		System.out.println("Discount amount: $" + df.format(rentPreDiscount * discount / 100));
		System.out.println("Final charge: $" + df.format(service.calculateRentWithDiscount(rentPreDiscount, discount)));
	}
}
