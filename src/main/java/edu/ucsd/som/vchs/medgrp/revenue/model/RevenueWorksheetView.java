/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

/**
 * Base class for both v_medgrp_revenue_worksheet and v_medgrp_revenue_worksheet_12months views.
 * 
 * @author somdev5
 *
 */
@Entity
@Table(name="v_medgrp_revenue_worksheet", catalog="budget")
@NamedStoredProcedureQueries( {
	@NamedStoredProcedureQuery(
			name=RevenueWorksheetView.GET_REVENUE_WORKSHEET_BY_DIV, 
			procedureName=RevenueWorksheetView.GET_REVENUE_WORKSHEET_BY_DIV,
			resultClasses = RevenueWorksheetView.class, 
			parameters = {@StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, name="pDivisionId")}),

	@NamedStoredProcedureQuery(
			name=RevenueWorksheetView.GET_REVENUE_WORKSHEET_BY_DIV_AND_SOS, 
			procedureName=RevenueWorksheetView.GET_REVENUE_WORKSHEET_BY_DIV_AND_SOS,
			resultClasses = RevenueWorksheetView.class, 
			parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, name="pDivisionId"),
				@StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name="pSos")
			})
})
public class RevenueWorksheetView extends RevenueWorksheetBase {

	/**
	 * Generated v_medgrp_revenue_worksheet_12months
	 */
	private static final long serialVersionUID = -3639317696915724896L;
	
	public static final String GET_REVENUE_WORKSHEET_BY_DIV = "spGetRevenueWorksheetByDivisionId";
	
	public static final String GET_REVENUE_WORKSHEET_BY_DIV_AND_SOS = "spGetRevenueWorksheetByDivisionIdAndSos";
	
	@Column(name="status", columnDefinition="CHAR")
	private String status;
	
	@Column(name="employee_fullname")
	private String employeeFullName;	
	
	@Column(name="employee_ucsd_id",columnDefinition="DECIMAL")
	private Integer employeeUcsdId;
	
	@Column(name="home_department_code")
	private String homeDepartmentCode;
	
	@Column(name="home_department_name")
	private String homeDepartmentName;	
	
	@Column(name="proj_charges")
	private BigDecimal projCharges;
	
	@Column(name="proj_collections")
	private BigDecimal projCollections;
	
	@Column(name="proj_trvus")
	private BigDecimal projTrvus;
	
	@Column(name="py_charges")
	private BigDecimal priorYearCharges;
	
	@Column(name="py_collections")
	private BigDecimal priorYearCollections;
	
	@Column(name="py_trvus")
	private BigDecimal priorYearTrvus;
	
	@Column(name="py_wrvus")
	private BigDecimal priorYearWrvus;	
	
	@Column(name="12mo_charges")
	private BigDecimal rollingCharges;
	
	@Column(name="12mo_collections")
	private BigDecimal rollingCollections;
	
	@Column(name="12mo_trvus")
	private BigDecimal rollingTrvus;
	
	@Column(name="12mo_wrvus")
	private BigDecimal rollingWrvus;
		
	@Column(name="wrvu_percent_change")
	private String wrvuPercentChange;	
	
	@Column(name="removable_flag")
	private String removable;
	
	@Column(name="12mo_only_flag")
	private String rollingOnly;

	/**
	 * Default constructor
	 */
	public RevenueWorksheetView() {
		super();
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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
	 * @return the homeDepartmentName
	 */
	public String getHomeDepartmentName() {
		return homeDepartmentName;
	}

	/**
	 * @param homeDepartmentName the homeDepartmentName to set
	 */
	public void setHomeDepartmentName(String homeDepartmentName) {
		this.homeDepartmentName = homeDepartmentName;
	}

	/**
	 * @return the projCharges
	 */
	public BigDecimal getProjCharges() {
		return projCharges;
	}

	/**
	 * @param projCharges the projCharges to set
	 */
	public void setProjCharges(BigDecimal projCharges) {
		this.projCharges = projCharges;
	}

	/**
	 * @return the projCollections
	 */
	public BigDecimal getProjCollections() {
		return projCollections;
	}

	/**
	 * @param projCollections the projCollections to set
	 */
	public void setProjCollections(BigDecimal projCollections) {
		this.projCollections = projCollections;
	}

	/**
	 * @return the projTrvus
	 */
	public BigDecimal getProjTrvus() {
		return projTrvus;
	}

	/**
	 * @param projTrvus the projTrvus to set
	 */
	public void setProjTrvus(BigDecimal projTrvus) {
		this.projTrvus = projTrvus;
	}
	
	/**
	 * @return the priorYearCharges
	 */
	public BigDecimal getPriorYearCharges() {
		return priorYearCharges;
	}

	/**
	 * @param priorYearCharges the priorYearCharges to set
	 */
	public void setPriorYearCharges(BigDecimal priorYearCharges) {
		this.priorYearCharges = priorYearCharges;
	}

	/**
	 * @return the priorYearCollections
	 */
	public BigDecimal getPriorYearCollections() {
		return priorYearCollections;
	}

	/**
	 * @param priorYearCollections the priorYearCollections to set
	 */
	public void setPriorYearCollections(BigDecimal priorYearCollections) {
		this.priorYearCollections = priorYearCollections;
	}

	/**
	 * @return the priorYearTrvus
	 */
	public BigDecimal getPriorYearTrvus() {
		return priorYearTrvus;
	}

	/**
	 * @param priorYearTrvus the priorYearTrvus to set
	 */
	public void setPriorYearTrvus(BigDecimal priorYearTrvus) {
		this.priorYearTrvus = priorYearTrvus;
	}

	/**
	 * @return the priorYearWrvus
	 */
	public BigDecimal getPriorYearWrvus() {
		return priorYearWrvus;
	}

	/**
	 * @param priorYearWrvus the priorYearWrvus to set
	 */
	public void setPriorYearWrvus(BigDecimal priorYearWrvus) {
		this.priorYearWrvus = priorYearWrvus;
	}

	/**
	 * @return the wrvuPercentChange
	 */
	public String getWrvuPercentChange() {
		return wrvuPercentChange;
	}

	/**
	 * @param wrvuPercentChange the wrvuPercentChange to set
	 */
	public void setWrvuPercentChange(String wrvuPercentChange) {
		this.wrvuPercentChange = wrvuPercentChange;
	}

	/**
	 * @return the rollingCharges
	 */
	public BigDecimal getRollingCharges() {
		return rollingCharges;
	}

	/**
	 * @param rollingCharges the rollingCharges to set
	 */
	public void setRollingCharges(BigDecimal rollingCharges) {
		this.rollingCharges = rollingCharges;
	}

	/**
	 * @return the rollingCollections
	 */
	public BigDecimal getRollingCollections() {
		return rollingCollections;
	}

	/**
	 * @param rollingCollections the rollingCollections to set
	 */
	public void setRollingCollections(BigDecimal rollingCollections) {
		this.rollingCollections = rollingCollections;
	}

	/**
	 * @return the rollingTrvus
	 */
	public BigDecimal getRollingTrvus() {
		return rollingTrvus;
	}

	/**
	 * @param rollingTrvus the rollingTrvus to set
	 */
	public void setRollingTrvus(BigDecimal rollingTrvus) {
		this.rollingTrvus = rollingTrvus;
	}

	/**
	 * @return the rollingWrvus
	 */
	public BigDecimal getRollingWrvus() {
		return rollingWrvus;
	}

	/**
	 * @param rollingWrvus the rollingWrvus to set
	 */
	public void setRollingWrvus(BigDecimal rollingWrvus) {
		this.rollingWrvus = rollingWrvus;
	}

	/**
	 * @return the removable
	 */
	public Boolean getRemovable() {
		return removable.equals("Y");
	}

	/**
	 * @param removable the removable to set
	 */
	public void setRemovable(String removable) {
		this.removable = removable;
	}
	
	/**
	 * @return the rollingOnlyFlag
	 */
	public Boolean getRollingOnly() {
		return rollingOnly.equals("Y");
	}

	/**
	 * @param rollingOnlyFlag the rollingOnlyFlag to set
	 */
	public void setRollingOnly(String rollingOnlyFlag) {
		this.rollingOnly = rollingOnlyFlag;
	}
	
	/**
	 * @return the employeeFullName
	 */
	public String getEmployeeFullName() {
		return employeeFullName;
	}

	/**
	 * @param employeeFullName the employeeFullName to set
	 */
	public void setEmployeeFullName(String employeeFullName) {
		this.employeeFullName = employeeFullName;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RevenueWorksheetViewBase [status=" + status
				+ ", homeDepartmentCode=" + homeDepartmentCode
				+ ", homeDepartmentName=" + homeDepartmentName
				+ ", projCharges=" + projCharges + ", projCollections="
				+ projCollections + ", projTrvus=" + projTrvus
				+ ", priorYearCharges=" + priorYearCharges
				+ ", priorYearCollections=" + priorYearCollections
				+ ", priorYearTrvus=" + priorYearTrvus + ", priorYearWrvus="
				+ priorYearWrvus + ", rollingCharges=" + rollingCharges
				+ ", rollingCollections=" + rollingCollections
				+ ", rollingTrvus=" + rollingTrvus + ", rollingWrvus="
				+ rollingWrvus + ", wrvuPercentChange=" + wrvuPercentChange
				+ "]";
	}
}
