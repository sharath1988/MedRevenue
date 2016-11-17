package edu.ucsd.som.vchs.medgrp.revenue.model.faculty;

public enum Rank {
	ASSOC_PROF("Assoc Prof"),
	ASST_PROF("Asst Prof"),
	INSTR("Instr"),
	MSP_ASSOC("MSP Assoc"),
	MSP_ASST("MSP Asst"),
	MSP_DIPLO("MSP Diplo"),
	MSP_SPEC("MSP Spec"),
	OTHER("Other"),
	PROFESSOR("Professor");
	
	private final String displayValue;
	
	private Rank(String displayValue) {
		this.displayValue = displayValue;
	}
	
	@Override
	public String toString() {
		return displayValue;
	}
}
