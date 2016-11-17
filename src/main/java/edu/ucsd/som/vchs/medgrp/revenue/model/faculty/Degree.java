package edu.ucsd.som.vchs.medgrp.revenue.model.faculty;

public enum Degree {
	DO("DO"), 
	MD("MD"), 
	MD_PHD("MD/PhD"), 
	NON_DOC("NonDoc"), 
	OTHER_DOC("OtherDoc"),
	PHD("PhD");
	
	private final String displayValue;
	
	private Degree(String value) {
		this.displayValue = value;
	}

	@Override
	public String toString() {
		return displayValue;
	}
}
