/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author myebba
 *
 */
@Entity
@Table(name="V_MEDGRP_REVENUE_CARE_PAYMENT_RATES",catalog="budget")
public class BillingAreaRate implements Serializable {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -6861972707197382580L;
	
	@Id
	@Column(name="ID")
	private String id;
	
	@Column(name="SOM_DIVISION_ID")
	private Integer somDivisionId;
	
	@Column(name="SOM_DIVISION_NAME")
	private String somDivisionName;
	
	@Column(name="TX_BILL_AREA_ID")
	private Integer billingAreaId;
	
	@Column(name="BILL_AREA_NAME")
	private String billingAreaName;
	
	@Column(name="RATE")
	private BigDecimal rate;
	
	@Column(name="BILLING_TYPE")
	private String billingType;
	
	@Column(name="LOGGED_IN_EIN", columnDefinition="BIGINT")
	private Integer loggedInUcsdId;

	/**
	 * Default constructor
	 */
	public BillingAreaRate() {
		super();
	}

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
	 * @return the loggedInUcsdId
	 */
	public Integer getLoggedInUcsdId() {
		return loggedInUcsdId;
	}

	/**
	 * @param loggedInUcsdId the loggedInUcsdId to set
	 */
	public void setLoggedInUcsdId(Integer loggedInUcsdId) {
		this.loggedInUcsdId = loggedInUcsdId;
	}
	
	/**
	 * @return the somDivisionName
	 */
	public String getSomDivisionName() {
		return somDivisionName;
	}

	/**
	 * @param somDivisionName the somDivisionName to set
	 */
	public void setSomDivisionName(String somDivisionName) {
		this.somDivisionName = somDivisionName;
	}

	/**
	 * @return the billingAreaName
	 */
	public String getBillingAreaName() {
		return billingAreaName;
	}

	/**
	 * @param billingAreaName the billingAreaName to set
	 */
	public void setBillingAreaName(String billingAreaName) {
		this.billingAreaName = billingAreaName;
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
				+ ((loggedInUcsdId == null) ? 0 : loggedInUcsdId.hashCode());
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
		BillingAreaRate other = (BillingAreaRate) obj;
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
		if (loggedInUcsdId == null) {
			if (other.loggedInUcsdId != null)
				return false;
		} else if (!loggedInUcsdId.equals(other.loggedInUcsdId))
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
		return "BillingAreaRate [id=" + id + ", somDivisionId=" + somDivisionId
				+ ", somDivisionName=" + somDivisionName + ", billingAreaId="
				+ billingAreaId + ", billingAreaName=" + billingAreaName
				+ ", rate=" + rate + ", billingType=" + billingType
				+ ", loggedInUcsdId=" + loggedInUcsdId + "]";
	}
}
