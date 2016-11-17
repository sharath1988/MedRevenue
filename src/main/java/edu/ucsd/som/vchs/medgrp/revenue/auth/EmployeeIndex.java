/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.auth;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity POJO for determining if user has access to an index
 * 
 * @author somdev5
 *
 */
@Entity
@Table(name="emp_index",catalog="budget")
public class EmployeeIndex implements Serializable {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 693279510372925975L;

	/**
	 * Default constructor
	 */
	public EmployeeIndex() {
		super();
	}
		
	@Id
	@Column(name="emp_index_id")
	private Integer empIndexId;
	
	@Column(name="parent_index")
	private String index;
	
	@Column(name="employee_ucsd_id")
	private Integer employeeUcsdId;

	public Integer getEmpIndexId() {
		return empIndexId;
	}

	public void setEmpIndexId(Integer empIndexId) {
		this.empIndexId = empIndexId;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public Integer getEmployeeUcsdId() {
		return employeeUcsdId;
	}

	public void setEmployeeUcsdId(Integer employeeUcsdId) {
		this.employeeUcsdId = employeeUcsdId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((employeeUcsdId == null) ? 0 : employeeUcsdId.hashCode());
		result = prime * result + ((index == null) ? 0 : index.hashCode());
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
		EmployeeIndex other = (EmployeeIndex) obj;
		if (employeeUcsdId == null) {
			if (other.employeeUcsdId != null)
				return false;
		} else if (!employeeUcsdId.equals(other.employeeUcsdId))
			return false;
		if (index == null) {
			if (other.index != null)
				return false;
		} else if (!index.equals(other.index))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EmployeeIndex [empIndexId=" + empIndexId + ", index=" + index
				+ ", employeeUcsdId=" + employeeUcsdId + "]";
	}
}
