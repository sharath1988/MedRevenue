package edu.ucsd.som.vchs.medgrp.revenue.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.PrePersist;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;
import javax.persistence.Transient;

import edu.ucsd.som.vchs.medgrp.revenue.model.faculty.FacultyEmployeeInfo;

@NamedStoredProcedureQuery(
	name = FutureEmployee.ADD_FACULTY_TBN_INFO,
	procedureName = "add_faculty_tbn_info",
	parameters = {
		@StoredProcedureParameter(mode=ParameterMode.IN, name="p_all_employee_id", type=Integer.class),
		@StoredProcedureParameter(mode=ParameterMode.IN, name="p_degree", type=String.class),
		@StoredProcedureParameter(mode=ParameterMode.IN, name="p_compPlan", type=String.class),
		@StoredProcedureParameter(mode=ParameterMode.IN, name="p_rank", type=String.class),
		@StoredProcedureParameter(mode=ParameterMode.IN, name="p_step", type=String.class),
		@StoredProcedureParameter(mode=ParameterMode.IN, name="p_offScale", type=Boolean.class),
		@StoredProcedureParameter(mode=ParameterMode.IN, name="p_appointment", type=BigDecimal.class)
	}
)
@Entity
@Table(name="future_employee",catalog="dw")
public class FutureEmployee {
	public static final String ADD_FACULTY_TBN_INFO = "createFacultyTbn";
	
	@PrePersist
	private void create() {
		creationTime = new Date();
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="tbn_id")
	private Integer tbnId;
	
	@Column(name="name")
	private String name;
	
	@Column(name="home_department_code")
	private String homeDepartmentCode;
	
	@Column(name="creation_time", columnDefinition="DATETIME")
	private Date creationTime;
	
	@Column(name="creation_application")
	private String creationApplication = "medgrp-revenue";
	
	@Transient
	private FutureEmployeeType futureEmployeeType;
	
	@Transient
	private FacultyEmployeeInfo facultyEmployeeInfo = new FacultyEmployeeInfo();

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

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the homeDepartmentCode
	 */
	public String getHomeDepartmentCode() {
		return homeDepartmentCode;
	}

	/**
	 * @param homeDepartmentCode the homeDepartmentCode to set
	 */
	public void setHomeDepartmentCode(String homeDepartmentCode) {
		this.homeDepartmentCode = homeDepartmentCode;
	}

	/**
	 * @return the creationTime
	 */
	public Date getCreationTime() {
		return creationTime;
	}

	/**
	 * @param creationTime the creationTime to set
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * @return the creationApplication
	 */
	public String getCreationApplication() {
		return creationApplication;
	}

	/**
	 * @param creationApplication the creationApplication to set
	 */
	public void setCreationApplication(String creationApplication) {
		this.creationApplication = creationApplication;
	}

	/**
	 * @return the futureEmployeeType
	 */
	public FutureEmployeeType getFutureEmployeeType() {
		return futureEmployeeType;
	}

	/**
	 * @param futureEmployeeType the futureEmployeeType to set
	 */
	public void setFutureEmployeeType(FutureEmployeeType futureEmployeeType) {
		this.futureEmployeeType = futureEmployeeType;
	}

	/**
	 * @return the facultyEmployeeInfo
	 */
	public FacultyEmployeeInfo getFacultyEmployeeInfo() {
		return facultyEmployeeInfo;
	}

	/**
	 * @param facultyEmployeeInfo the facultyEmployeeInfo to set
	 */
	public void setFacultyEmployeeInfo(FacultyEmployeeInfo facultyEmployeeInfo) {
		this.facultyEmployeeInfo = facultyEmployeeInfo;
	}
}
