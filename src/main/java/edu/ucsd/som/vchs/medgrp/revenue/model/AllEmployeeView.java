package edu.ucsd.som.vchs.medgrp.revenue.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

@NamedStoredProcedureQuery(
	name = AllEmployeeView.FIND_ADDABLE_PROVIDERS_QUERY_NAME,
	resultClasses = AllEmployeeView.class,
	procedureName = "find_addable_providers",
	parameters = {
		@StoredProcedureParameter(mode=ParameterMode.IN, name="p_query", type=String.class),
		@StoredProcedureParameter(mode=ParameterMode.IN, name="p_division_id", type=Integer.class),
		@StoredProcedureParameter(mode=ParameterMode.IN, name="p_sos", type=String.class),
		@StoredProcedureParameter(mode=ParameterMode.IN, name="p_limit", type=Integer.class)
	}
)
@Entity
@Table(name="all_employee_view",catalog="dw")
public class AllEmployeeView {
	public static final String FIND_ADDABLE_PROVIDERS_QUERY_NAME = "FindAddableProviders";
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="all_employee_id")
	Integer allEmployeeId;
	
	@Column(name="employee_fullname")
	String employeeFullname;
	
	@Column(name="employee_ucsd_id")
	Integer employeeUcsdId;
	
	@Column(name="employee_reference_id", columnDefinition="DECIMAL")
	Integer employeeReferenceId;

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
	 * @return the employeeFullname
	 */
	public String getEmployeeFullname() {
		return employeeFullname;
	}

	/**
	 * @param employeeFullname the employeeFullname to set
	 */
	public void setEmployeeFullname(String employeeFullname) {
		this.employeeFullname = employeeFullname;
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
	 * @return the employeeReferenceId
	 */
	public Integer getEmployeeReferenceId() {
		return employeeReferenceId;
	}

	/**
	 * @param employeeReferenceId the employeeReferenceId to set
	 */
	public void setEmployeeReferenceId(Integer employeeReferenceId) {
		this.employeeReferenceId = employeeReferenceId;
	}
}
