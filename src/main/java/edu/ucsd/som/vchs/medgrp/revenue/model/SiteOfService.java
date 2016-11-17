/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.model;

/**
 * Enum for Site of Service (aka sos)
 * 
 * @author somdev5
 *
 */
public enum SiteOfService {
	INPATIENT("INPATIENT"), OUTPATIENT("OUTPATIENT"), BOTH("INP/OUTP");
	
	private String value;
		
	private SiteOfService(String value) {
		this.value = value;
	}
	
	public Boolean isInpatient() {
		return equals(INPATIENT);
	}
	
	public Boolean isOutpatient() {
		return equals(OUTPATIENT);
	}
	
	public Boolean isBoth() {
		return equals(BOTH);
	}
	
	@Override
	public String toString() {
		return this.value;
	}
}