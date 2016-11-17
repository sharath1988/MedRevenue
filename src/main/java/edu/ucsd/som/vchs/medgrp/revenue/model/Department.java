package edu.ucsd.som.vchs.medgrp.revenue.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.Table;

@Entity
@Table(name="ucsd_department",catalog="som_portal")
public class Department {
	@PostLoad
	private void repair() {
		if (departmentName != null) {
			departmentName = departmentName.trim();
		}
	}
	
	@Id
	@Column(name="deptCode",columnDefinition="char")
	private String departmentCode;
	
	@Column(name="deptName")
	private String departmentName;
	/**
	 * @return the departmentCode
	 */
	public String getDepartmentCode() {
		return departmentCode;
	}
	/**
	 * @param departmentCode the departmentCode to set
	 */
	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}
	/**
	 * @return the departmentName
	 */
	public String getDepartmentName() {
		return departmentName;
	}
	/**
	 * @param departmentName the departmentName to set
	 */
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	
	@Override
	public String toString() {
		return departmentCode;
	}
}
