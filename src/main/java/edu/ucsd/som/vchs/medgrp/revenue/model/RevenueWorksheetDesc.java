package edu.ucsd.som.vchs.medgrp.revenue.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQuery(name=RevenueWorksheetDesc.FIND_ID_BY_ALL_EMPLOYEE_ID,query="select wd.worksheetDescId from RevenueWorksheetDesc wd where wd.allEmployeeId = ?1")
@Entity
@Table(name="medgrp_revenue_worksheet_desc")
public class RevenueWorksheetDesc {
	public static final String FIND_ID_BY_ALL_EMPLOYEE_ID = "findIdByAllEmployeeId";
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="worksheet_desc_id")
	private Integer worksheetDescId;
	
	@Column(name="description")
	private String description;
	
	@Column(name="all_employee_id")
	private Integer allEmployeeId;
	
	@Column(name="reference_number")
	private Integer referenceNumber;

	/**
	 * @return the worksheetDescId
	 */
	public Integer getWorksheetDescId() {
		return worksheetDescId;
	}

	/**
	 * @param worksheetDescId the worksheetDescId to set
	 */
	public void setWorksheetDescId(Integer worksheetDescId) {
		this.worksheetDescId = worksheetDescId;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the allEmployeeId
	 */
	public Integer getAllEmployeeId() {
		return allEmployeeId;
	}

	/**
	 * @param allEmployeeId the allEmployeeId to set
	 */
	public void setAllEmployeeId(Integer allEmployeeId) {
		this.allEmployeeId = allEmployeeId;
	}

	/**
	 * @return the referenceNumber
	 */
	public Integer getReferenceNumber() {
		return referenceNumber;
	}

	/**
	 * @param referenceNumber the referenceNumber to set
	 */
	public void setReferenceNumber(Integer referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
}
