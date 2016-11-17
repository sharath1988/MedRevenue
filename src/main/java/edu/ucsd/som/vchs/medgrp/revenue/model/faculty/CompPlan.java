package edu.ucsd.som.vchs.medgrp.revenue.model.faculty;

public enum CompPlan {
	HSCP("HSCP"), 
	NONE("None"), 
	SPC("SPC"), 
	VA_UC("VA/UC");
	
	private final String displayValue;
	
	private CompPlan(String displayValue) {
		this.displayValue = displayValue;
	}
	
	@Override
	public String toString() {
		return displayValue;
	}
}
