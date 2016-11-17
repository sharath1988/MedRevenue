/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * POJO for Care Payment tab data model
 * 
 * @author myebba
 *
 */
@Entity
@IdClass(CarePaymentDetailPK.class)
@Table(name="V_MEDGRP_REVENUE_CARE_PAYMENT_DETAIL", catalog="budget")
public class CarePaymentDetail implements Serializable {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 8776757094587190548L;

	/**
	 * Default constructor 
	 */
	public CarePaymentDetail() {
		super();
	}
	
	@Id
	@Column(name="SOM_DIVISION_ID")
	private Integer somDivisionId;

	@Id
	@Column(name="EMPLOYEE_UCSD_ID", columnDefinition="BIGINT")
	private Integer employeeUcsdId;
	
	@Id
	@Column(name="EMPLOYEE_NAME")
	private String employeeName;
	
	@Id
	@Column(name="TX_BILL_AREA_ID", columnDefinition="BIGINT")
	private Integer billingAreaId;
	
	@Id
	@Column(name="BILLING_TYPE")
	private String billingType;
	
	@Id
	@Column(name="PROJ_WRVUS")
	private BigDecimal projectedWrvus;
	
	@Id
	@Column(name="PROJ_ASAUNITS")
	private BigDecimal projectedAsaUnits;
	
	@Column(name="BILLING_TYPE_PERC")
	private BigDecimal billingTypePerc;
	
	@Column(name="HIST_WRVU_PERC")
	private BigDecimal divisionCyWrvuPerc;
	
	@Column(name="RATE")
	private BigDecimal rate;
	
	@Column(name="ASA_RATE")
	private BigDecimal asaRate;
	
	@Column(name="ALL_EMP_ID")
	private Integer allEmployeeId;
	
	@Column(name="PROJ_CARE_PAYMENT_AMOUNT")
	private BigDecimal carePaymentAmount;
	
	@Column(name="TOTAL_PROJ_CARE_PAYMENT_AMOUNT")
	private BigDecimal totalCarePaymentAmount;
	
	@Column(name="TOTAL_SORT_COL")
	private String totalSortColumn;

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
	 * @return the employeeName
	 */
	public String getEmployeeName() {
		return employeeName;
	}

	/**
	 * @param employeeName the employeeName to set
	 */
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	/**
	 * @return the somDivisionId
	 */
	public Integer getSomDivisionId() {
		return somDivisionId;
	}

	/**
	 * @param somDivisionId the somDivisionId to set
	 */
	public void setSomDivisionId(Integer somDivisionId) {
		this.somDivisionId = somDivisionId;
	}

	/**
	 * @return the billingAreaId
	 */
	public Integer getBillingAreaId() {
		return billingAreaId;
	}

	/**
	 * @param billingAreaId the billingAreaId to set
	 */
	public void setBillingAreaId(Integer billingAreaId) {
		this.billingAreaId = billingAreaId;
	}

	/**
	 * @return the billingType
	 */
	public String getBillingType() {
		return billingType;
	}

	/**
	 * @param billingType the billingType to set
	 */
	public void setBillingType(String billingType) {
		this.billingType = billingType;
	}

	/**
	 * @return the billingTypePerc
	 */
	public BigDecimal getBillingTypePerc() {
		return billingTypePerc;
	}

	/**
	 * @param billingTypePerc the billingTypePerc to set
	 */
	public void setBillingTypePerc(BigDecimal billingTypePerc) {
		this.billingTypePerc = billingTypePerc;
	}

	/**
	 * @return the divisionCyWrvuPerc
	 */
	public BigDecimal getDivisionCyWrvuPerc() {
		return divisionCyWrvuPerc;
	}

	/**
	 * @param divisionCyWrvuPerc the divisionCyWrvuPerc to set
	 */
	public void setDivisionCyWrvuPerc(BigDecimal divisionCyWrvuPerc) {
		this.divisionCyWrvuPerc = divisionCyWrvuPerc;
	}

	/**
	 * @return the rate
	 */
	public BigDecimal getRate() {
		return rate;
	}

	/**
	 * @param rate the rate to set
	 */
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	/**
	 * @return the projectedWrvus
	 */
	public BigDecimal getProjectedWrvus() {
		return projectedWrvus;
	}

	/**
	 * @param projectedWrvus the projectedWrvus to set
	 */
	public void setProjectedWrvus(BigDecimal projectedWrvus) {
		this.projectedWrvus = projectedWrvus;
	}

	/**
	 * @return the carePaymentAmount
	 */
	public BigDecimal getCarePaymentAmount() {
		return carePaymentAmount;
	}

	/**
	 * @param carePaymentAmount the carePaymentAmount to set
	 */
	public void setCarePaymentAmount(BigDecimal carePaymentAmount) {
		this.carePaymentAmount = carePaymentAmount;
	}

	/**
	 * @return the totalCarePaymentAmount
	 */
	public BigDecimal getTotalCarePaymentAmount() {
		return totalCarePaymentAmount;
	}

	/**
	 * @param totalCarePaymentAmount the totalCarePaymentAmount to set
	 */
	public void setTotalCarePaymentAmount(BigDecimal totalCarePaymentAmount) {
		this.totalCarePaymentAmount = totalCarePaymentAmount;
	}
	
	/**
	 * @return the totalSortColumn
	 */
	public String getTotalSortColumn() {
		return totalSortColumn;
	}

	/**
	 * @param totalSortColumn the totalSortColumn to set
	 */
	public void setTotalSortColumn(String totalSortColumn) {
		this.totalSortColumn = totalSortColumn;
	}
	
	/**
	 * @return the projectAsaUnits
	 */
	public BigDecimal getProjectedAsaUnits() {
		return projectedAsaUnits;
	}

	/**
	 * @param projectAsaUnits the projectAsaUnits to set
	 */
	public void setProjectedAsaUnits(BigDecimal projectAsaUnits) {
		this.projectedAsaUnits = projectAsaUnits;
	}

	/**
	 * @return the asaRate
	 */
	public BigDecimal getAsaRate() {
		return asaRate;
	}

	/**
	 * @param asaRate the asaRate to set
	 */
	public void setAsaRate(BigDecimal asaRate) {
		this.asaRate = asaRate;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((billingAreaId == null) ? 0 : billingAreaId.hashCode());
		result = prime * result
				+ ((billingType == null) ? 0 : billingType.hashCode());
		result = prime * result
				+ ((employeeName == null) ? 0 : employeeName.hashCode());
		result = prime * result
				+ ((employeeUcsdId == null) ? 0 : employeeUcsdId.hashCode());
		result = prime * result
				+ ((projectedAsaUnits == null) ? 0 : projectedAsaUnits.hashCode());
		result = prime * result
				+ ((projectedWrvus == null) ? 0 : projectedWrvus.hashCode());
		result = prime * result
				+ ((somDivisionId == null) ? 0 : somDivisionId.hashCode());
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
		CarePaymentDetail other = (CarePaymentDetail) obj;
		if (billingAreaId == null) {
			if (other.billingAreaId != null)
				return false;
		} else if (!billingAreaId.equals(other.billingAreaId))
			return false;
		if (billingType == null) {
			if (other.billingType != null)
				return false;
		} else if (!billingType.equals(other.billingType))
			return false;
		if (employeeName == null) {
			if (other.employeeName != null)
				return false;
		} else if (!employeeName.equals(other.employeeName))
			return false;
		if (employeeUcsdId == null) {
			if (other.employeeUcsdId != null)
				return false;
		} else if (!employeeUcsdId.equals(other.employeeUcsdId))
			return false;
		if (projectedAsaUnits == null) {
			if (other.projectedAsaUnits != null)
				return false;
		} else if (!projectedAsaUnits.equals(other.projectedAsaUnits))
			return false;
		if (projectedWrvus == null) {
			if (other.projectedWrvus != null)
				return false;
		} else if (!projectedWrvus.equals(other.projectedWrvus))
			return false;
		if (somDivisionId == null) {
			if (other.somDivisionId != null)
				return false;
		} else if (!somDivisionId.equals(other.somDivisionId))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CarePaymentDetail [employeeUcsdId=" + employeeUcsdId
				+ ", employeeName=" + employeeName + ", somDivisionId="
				+ somDivisionId + ", billingAreaId=" + billingAreaId
				+ ", billingType=" + billingType + ", rate=" + rate
				+ ", projectedWrvus=" + projectedWrvus + ", carePaymentAmount="
				+ carePaymentAmount + "]";
	}
}
