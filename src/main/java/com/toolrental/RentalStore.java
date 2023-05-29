package com.toolrental;

import java.time.LocalDate;
import java.util.Scanner;

import com.toolrental.entity.RentalAgreement;
import com.toolrental.entity.Tool;
import com.toolrental.exceptions.ToolRentalException;
import com.toolrental.service.Service;

//vk0523

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

		RentalAgreement agreement = service.generateAgreement(tool, rentalDays, discount, date);
		agreement.printRentalAgreement();
	}
}
