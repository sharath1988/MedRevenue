package edu.ucsd.som.vchs.medgrp.revenue.model;

public enum RevenueWorksheetMode {
	LAST_CALENDAR_YEAR, ROLLING_12_MONTHS;
	
	public Boolean getLastCalendarYear() {
		return equals(RevenueWorksheetMode.LAST_CALENDAR_YEAR);
	}
	public Boolean getRolling12Months() {
		return equals(RevenueWorksheetMode.ROLLING_12_MONTHS);
	}
}
