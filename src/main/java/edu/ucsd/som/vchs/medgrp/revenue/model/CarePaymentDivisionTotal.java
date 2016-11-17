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
 * POJO for Care Payment tab data model
 * 
 * @author myebba
 *
 */
@Entity
@Table(name="V_MEDGRP_REVENUE_CARE_PAYMENT_TOTAL_BY_DIV", catalog="budget")
public class CarePaymentDivisionTotal implements Serializable {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -7648318345446785009L;


	/**
	 * Default constructor 
	 */
	public CarePaymentDivisionTotal() {
		super();
	}
	
	@Id
	@Column(name="SOM_DIVISION_ID")
	private Integer somDivisionId;

	
	@Column(name="PROJ_CARE_PAYMENT_AMOUNT")
	private BigDecimal carePaymentAmount;


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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		CarePaymentDivisionTotal other = (CarePaymentDivisionTotal) obj;
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
		return "CarePaymentDivisionTotal [somDivisionId=" + somDivisionId
				+ ", carePaymentAmount=" + carePaymentAmount + "]";
	}
}
