package com.toolrental.entity;

import java.math.RoundingMode;
import java.text.DecimalFormat;

//vk0523

public class RentalAgreement {
	private Tool tool;
	private String toolCode;
	private String toolType;
	private String toolBrand;
	private Integer rentalDays;
	private String checkoutDate;
	private String dueDate;
	private double dailyRentalCharge;
	private int chargeDays;
	private double preDiscountCharge;
	private int discountPercent;
	private double discountAmount;
	private double finalCharge;

	public RentalAgreement() {
	}

	public RentalAgreement(Tool tool) {
		this.tool = tool;
	}

	public Tool getTool() {
		return tool;
	}

	public void setTool(Tool tool) {
		this.tool = tool;
	}

	public String getToolCode() {
		return toolCode;
	}

	public void setToolCode(String toolCode) {
		this.toolCode = toolCode;
	}

	public String getToolType() {
		return toolType;
	}

	public void setToolType(String toolType) {
		this.toolType = toolType;
	}

	public String getToolBrand() {
		return toolBrand;
	}

	public void setToolBrand(String toolBrand) {
		this.toolBrand = toolBrand;
	}

	public Integer getRentalDays() {
		return rentalDays;
	}

	public void setRentalDays(Integer rentalDays) {
		this.rentalDays = rentalDays;
	}

	public String getCheckoutDate() {
		return checkoutDate;
	}

	public void setCheckoutDate(String checkoutDate) {
		this.checkoutDate = checkoutDate;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public double getDailyRentalCharge() {
		return dailyRentalCharge;
	}

	public void setDailyRentalCharge(Double dailyCarge) {
		this.dailyRentalCharge = dailyCarge;
	}

	public int getChargeDays() {
		return chargeDays;
	}

	public void setChargeDays(int chargeDays) {
		this.chargeDays = chargeDays;
	}

	public double getPreDiscountCharge() {
		return preDiscountCharge;
	}

	public void setPreDiscountCharge(double preDiscountCharge) {
		this.preDiscountCharge = preDiscountCharge;
	}

	public int getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(int discountPercent) {
		this.discountPercent = discountPercent;
	}

	public double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public double getFinalCharge() {
		return finalCharge;
	}

	public void setFinalCharge(double finalCharge) {
		this.finalCharge = finalCharge;
	}

	public void printRentalAgreement() {
		System.out.println("Tool code: " + toolCode);
		System.out.println("Tool type: " + toolType);
		System.out.println("Tool Brand: " + toolBrand);
		System.out.println("Rental days: " + rentalDays);
		System.out.println("Check out date: " + checkoutDate);
		System.out.println("Due date: " + dueDate);
		System.out.println("Daily rental charge: " + dailyRentalCharge);
		System.out.println("Charge days: " + chargeDays);
		DecimalFormat df = new DecimalFormat("##,##0.00");
		df.setRoundingMode(RoundingMode.UP);
		System.out.println("Pre-discount charge: $" + df.format(preDiscountCharge));
		System.out.println("Discount percent: " + discountPercent + "%");
		System.out.println("Discount amount: $" + df.format(discountAmount));
		System.out.println("Final charge: $" + df.format(finalCharge));
	}
}
