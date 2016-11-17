/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * CarePayment Primary Key class
 * 
 * @author myebba
 *
 */
public class CarePaymentDetailPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6842069943431996348L;

	/**
	 * Default constructor 
	 */
	public CarePaymentDetailPK() {
		super();
	}
	
	private Integer somDivisionId;

	private Integer employeeUcsdId;
	
	private String employeeName;
	
	private Integer billingAreaId;
	
	private String billingType;
		
	private BigDecimal projectedWrvus;
	
	private BigDecimal projectedAsaUnits;

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
	 * @return the projectAsaUnits
	 */
	public BigDecimal getProjectAsaUnits() {
		return projectedAsaUnits;
	}

	/**
	 * @param projectAsaUnits the projectAsaUnits to set
	 */
	public void setProjectAsaUnits(BigDecimal projectedAsaUnits) {
		this.projectedAsaUnits = projectedAsaUnits;
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
		CarePaymentDetailPK other = (CarePaymentDetailPK) obj;
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
		return "CarePaymentDetailPK [somDivisionId=" + somDivisionId
				+ ", employeeUcsdId=" + employeeUcsdId + ", employeeName="
				+ employeeName + ", billingAreaId=" + billingAreaId
				+ ", billingType=" + billingType + ", projectedWrvus="
				+ projectedWrvus + ", projectAsaUnits=" + projectedAsaUnits + "]";
	}
}
