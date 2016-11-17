/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.auth;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Entity for the Revenue division list page
 * 
 * @author somdev5
 *
 */
@Entity
@Table(name="v_employee_division", catalog="budget")
public class EmployeeDivision implements Serializable {
	
	/**
	 * Generated
	 */
	private static final long serialVersionUID = -664185480720812726L;
	
	public static final String FIND_ALL_BY_USER_ID = "MedGrpRevenueDivision.FindAll";
	
	/**
	 * Default constructor
	 */
	public EmployeeDivision() {
		super();
	}
	
	@Id
	@Column(name="id")
	private String id;

	@Column(name="department_id")
	private Integer departmentId;
	
	@Column(name="department_name")
	private String departmentName;
	
	@Column(name="division_id")
	private Integer divisionId;
	
	@Column(name="division_name")
	private String divisionName;
	
	@Column(name="employee_ucsd_id", columnDefinition="BIGINT")
	private Integer employeeUcsdId;
	
	@Column(name="group_area")
	private String groupArea;
	
	@Column(name="provider_count", columnDefinition="BIGINT")
	private Integer providerCount;
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the departmentId
	 */
	public Integer getDepartmentId() {
		return departmentId;
	}

	/**
	 * @param departmentId the departmentId to set
	 */
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
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

	/**
	 * @return the divisionId
	 */
	public Integer getDivisionId() {
		return divisionId;
	}

	/**
	 * @param divisionId the divisionId to set
	 */
	public void setDivisionId(Integer divisionId) {
		this.divisionId = divisionId;
	}

	/**
	 * @return the divisionName
	 */
	public String getDivisionName() {
		return divisionName;
	}

	/**
	 * @param divisionName the divisionName to set
	 */
	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
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
	 * @return the groupArea
	 */
	public String getGroupArea() {
		return groupArea;
	}

	/**
	 * @param groupArea the groupArea to set
	 */
	public void setGroupArea(String groupArea) {
		this.groupArea = groupArea;
	}
	
	/**
	 * @return the providerCount
	 */
	public Integer getProviderCount() {
		return providerCount;
	}

	/**
	 * @param providerCount the providerCount to set
	 */
	public void setProviderCount(Integer providerCount) {
		this.providerCount = providerCount;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((divisionId == null) ? 0 : divisionId.hashCode());
		result = prime * result
				+ ((employeeUcsdId == null) ? 0 : employeeUcsdId.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmployeeDivision other = (EmployeeDivision) obj;
		if (divisionId == null) {
			if (other.divisionId != null)
				return false;
		} else if (!divisionId.equals(other.divisionId))
			return false;
		if (employeeUcsdId == null) {
			if (other.employeeUcsdId != null)
				return false;
		} else if (!employeeUcsdId.equals(other.employeeUcsdId))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MedGrpRevenueDivision [departmentId=" + departmentId
				+ ", departmentName=" + departmentName + ", divisionId="
				+ divisionId + ", divisionName=" + divisionName
				+ ", employeeUcsdId=" + employeeUcsdId + "]";
	}
	
	/**
	 * 
	 * @return
	 */
	@Transient
	public Boolean getIsAnesthesiology() {
		return getDivisionId().equals(1);
	}

	/**
	 * 
	 * @return
	 */
	@Transient
	public Boolean getIsPsychiatry() {
		return getDivisionId().equals(22);
	}	
}