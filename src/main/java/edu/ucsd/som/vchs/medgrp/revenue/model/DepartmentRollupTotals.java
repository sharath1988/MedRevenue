package edu.ucsd.som.vchs.medgrp.revenue.model;

import java.math.BigDecimal;

public class DepartmentRollupTotals {
	private BigDecimal projectedCharges = BigDecimal.ZERO;
	private BigDecimal projectedCollections = BigDecimal.ZERO;
	private BigDecimal projectedTRVUs = BigDecimal.ZERO;
	private BigDecimal projectedWRVUs = BigDecimal.ZERO;
	private BigDecimal priorYearCharges = BigDecimal.ZERO;
	private BigDecimal priorYearCollections = BigDecimal.ZERO;
	private BigDecimal priorYearTRVUs = BigDecimal.ZERO;
	private BigDecimal priorYearWRVUs = BigDecimal.ZERO;
	
	public BigDecimal getProjectedCharges() {
		return projectedCharges;
	}
	public void setProjectedCharges(BigDecimal projectedCharges) {
		this.projectedCharges = projectedCharges;
	}
	public BigDecimal getProjectedCollections() {
		return projectedCollections;
	}
	public void setProjectedCollections(BigDecimal projectedCollections) {
		this.projectedCollections = projectedCollections;
	}
	public BigDecimal getProjectedTRVUs() {
		return projectedTRVUs;
	}
	public void setProjectedTRVUs(BigDecimal projectedTRVUs) {
		this.projectedTRVUs = projectedTRVUs;
	}
	public BigDecimal getProjectedWRVUs() {
		return projectedWRVUs;
	}
	public void setProjectedWRVUs(BigDecimal projectedWRVUs) {
		this.projectedWRVUs = projectedWRVUs;
	}
	public BigDecimal getPriorYearCharges() {
		return priorYearCharges;
	}
	public void setPriorYearCharges(BigDecimal priorYearCharges) {
		this.priorYearCharges = priorYearCharges;
	}
	public BigDecimal getPriorYearCollections() {
		return priorYearCollections;
	}
	public void setPriorYearCollections(BigDecimal priorYearCollections) {
		this.priorYearCollections = priorYearCollections;
	}
	public BigDecimal getPriorYearTRVUs() {
		return priorYearTRVUs;
	}
	public void setPriorYearTRVUs(BigDecimal priorYearTRVUs) {
		this.priorYearTRVUs = priorYearTRVUs;
	}
	public BigDecimal getPriorYearWRVUs() {
		return priorYearWRVUs;
	}
	public void setPriorYearWRVUs(BigDecimal priorYearWRVUs) {
		this.priorYearWRVUs = priorYearWRVUs;
	}
}
