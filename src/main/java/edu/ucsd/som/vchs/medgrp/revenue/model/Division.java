/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * POJO for returning a division to department mapping
 * 
 * Currently pointing to v_som_division but can write another view
 * that performs div->dept name mapping from another source if necessary
 * 
 * @author somdev5
 *
 */
@Entity
@Table(name="v_som_division", catalog="budget")
public class Division implements Serializable {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 2146712747446881762L;

	/**
	 * Default constructor
	 */
	public Division() {
		super();
	}

	@Id
	@Column(name="division_id")
	private Integer divisionId;
	
	@Column(name="division_name")
	private String divisionName;
	
	@Column(name="department_id")
	private Integer departmentId;
	
	@Column(name="department_name")
	private String departmentName;

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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((divisionId == null) ? 0 : divisionId.hashCode());
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
		Division other = (Division) obj;
		if (divisionId == null) {
			if (other.divisionId != null)
				return false;
		} else if (!divisionId.equals(other.divisionId))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Division [divisionId=" + divisionId + ", divisionName="
				+ divisionName + ", departmentId=" + departmentId
				+ ", departmentName=" + departmentName + "]";
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
		//return getDivisionId().equals(22);
		return Boolean.FALSE;
	}
}
