package edu.ucsd.som.vchs.medgrp.revenue.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(DepartmentViewGrouping.class)
@Table(name="v_medgrp_revenue_dept", catalog="budget")
public class DepartmentView {
	@Id
	private Integer departmentId;
	
	@Id
	private String departmentName;
	
	@Id
	private Integer divisionId;
	
	@Id
	private String divisionName;
	
	@Column(name="proj_charges")
	private BigDecimal projectedCharges;
	
	@Column(name="proj_collections")
	private BigDecimal projectedCollections;
	
	@Column(name="proj_trvus")
	private BigDecimal projectedTRVUs;
	
	@Column(name="proj_wrvus")
	private BigDecimal projectedWRVUs;
	
	@Column(name="py_actual_charges")
	private BigDecimal priorYearCharges;
	
	@Column(name="py_actual_collections")
	private BigDecimal priorYearCollections;
	
	@Column(name="py_actual_trvus")
	private BigDecimal priorYearTRVUs;
	
	@Column(name="py_actual_wrvus")
	private BigDecimal priorYearWRVUs;

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Integer getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Integer divisionId) {
		this.divisionId = divisionId;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

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