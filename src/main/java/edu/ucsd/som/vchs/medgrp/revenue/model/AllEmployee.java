package edu.ucsd.som.vchs.medgrp.revenue.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="all_employee",catalog="dw")
public class AllEmployee {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="all_employee_id")
	private Integer allEmployeeId;
	
	@Column(name="employee_ucsd_id")
	private Integer employeeUcsdId;
	
	@Column(name="tbn_id")
	private Integer tbnId;

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
	 * @return the employeeUcsdId
	 */
	public Integer getEmployeeUcsdId() {
		return employeeUcsdId;
	}

	/**
	 * @param employeeUcsdId the employeeUcsdId to set
	 */
	public void setEmployeeUcsdId(Integer employeeUcsdId) {
		this.employeeUcsdId = employeeUcsdId;
	}

	/**
	 * @return the tbnId
	 */
	public Integer getTbnId() {
		return tbnId;
	}

	/**
	 * @param tbnId the tbnId to set
	 */
	public void setTbnId(Integer tbnId) {
		this.tbnId = tbnId;
	}
}
