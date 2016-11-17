package edu.ucsd.som.vchs.medgrp.revenue.model;

public enum FutureEmployeeType {
	FACULTY("Faculty"), OTHER("Other");
	
	private final String displayValue;
	
	private FutureEmployeeType(String displayValue) {
		this.displayValue = displayValue;
	}
	
	@Override
	public String toString() {
		return displayValue;
	}
}
