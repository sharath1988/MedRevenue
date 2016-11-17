package edu.ucsd.som.vchs.medgrp.revenue.model;

import java.io.Serializable;

import javax.persistence.Column;

public class DepartmentViewGrouping implements Serializable {
	private static final long serialVersionUID = 995930189861835445L;
	
	@Column(name="department_id")
	private Integer departmentId;
	
	@Column(name="department_name")
	private String departmentName;
	
	@Column(name="division_id")
	private Integer divisionId;
	
	@Column(name="division_name")
	private String divisionName;
	
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((departmentId == null) ? 0 : departmentId.hashCode());
		result = prime * result
				+ ((departmentName == null) ? 0 : departmentName.hashCode());
		result = prime * result
				+ ((divisionId == null) ? 0 : divisionId.hashCode());
		result = prime * result
				+ ((divisionName == null) ? 0 : divisionName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DepartmentViewGrouping other = (DepartmentViewGrouping) obj;
		if (departmentId == null) {
			if (other.departmentId != null)
				return false;
		} else if (!departmentId.equals(other.departmentId))
			return false;
		if (departmentName == null) {
			if (other.departmentName != null)
				return false;
		} else if (!departmentName.equals(other.departmentName))
			return false;
		if (divisionId == null) {
			if (other.divisionId != null)
				return false;
		} else if (!divisionId.equals(other.divisionId))
			return false;
		if (divisionName == null) {
			if (other.divisionName != null)
				return false;
		} else if (!divisionName.equals(other.divisionName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DepartmentViewGrouping [departmentId=" + departmentId
				+ ", departmentName=" + departmentName + ", divisionId="
				+ divisionId + ", divisionName=" + divisionName + "]";
	}
}
