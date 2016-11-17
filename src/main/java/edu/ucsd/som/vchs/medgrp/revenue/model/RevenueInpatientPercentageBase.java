/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Entity for displaying revenue inpatient percentages by division
 * 
 * @author somdev5
 *
 */
@MappedSuperclass
public abstract class RevenueInpatientPercentageBase implements Serializable {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -662599309089907450L;
	
	@Id
	@Column(name="division_id")
	private Integer divisionId;
	
	@Column(name="total_inpatient_trvus")
	private BigDecimal totalInpatientTrvus;
	
	@Column(name="total_div_trvus")
	private BigDecimal totalDivisionTrvus;
	
	@Column(name="prior_inpatient_percentage")
	private BigDecimal priorInpatientPercentage;
	
	@Column(name="proj_inpatient_percentage")
	private BigDecimal projInpatientPercentage;

	/**
	 * Default constructor
	 */
	public RevenueInpatientPercentageBase() {
		super();
	}

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
	 * @return the totalInpatientTrvus
	 */
	public BigDecimal getTotalInpatientTrvus() {
		return totalInpatientTrvus;
	}

	/**
	 * @param totalInpatientTrvus the totalInpatientTrvus to set
	 */
	public void setTotalInpatientTrvus(BigDecimal totalInpatientTrvus) {
		this.totalInpatientTrvus = totalInpatientTrvus;
	}

	/**
	 * @return the totalDivisionTrvus
	 */
	public BigDecimal getTotalDivisionTrvus() {
		return totalDivisionTrvus;
	}

	/**
	 * @param totalDivisionTrvus the totalDivisionTrvus to set
	 */
	public void setTotalDivisionTrvus(BigDecimal totalDivisionTrvus) {
		this.totalDivisionTrvus = totalDivisionTrvus;
	}

	/**
	 * @return the priorInpatientPercentage
	 */
	public BigDecimal getPriorInpatientPercentage() {
		return priorInpatientPercentage;
	}

	/**
	 * @param priorInpatientPercentage the priorInpatientPercentage to set
	 */
	public void setPriorInpatientPercentage(BigDecimal priorInpatientPercentage) {
		this.priorInpatientPercentage = priorInpatientPercentage;
	}

	/**
	 * @return the projInpatientPercentage
	 */
	public BigDecimal getProjInpatientPercentage() {
		return projInpatientPercentage;
	}

	/**
	 * @param projInpatientPercentage the projInpatientPercentage to set
	 */
	public void setProjInpatientPercentage(BigDecimal projInpatientPercentage) {
		this.projInpatientPercentage = projInpatientPercentage;
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
		RevenueInpatientPercentageBase other = (RevenueInpatientPercentageBase) obj;
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
		return "RevenueInpatientPercentage [divisionId=" + divisionId
				+ ", totalInpatientTrvus=" + totalInpatientTrvus
				+ ", totalDivisionTrvus=" + totalDivisionTrvus
				+ ", priorInpatientPercentage=" + priorInpatientPercentage
				+ ", projInpatientPercentage=" + projInpatientPercentage + "]";
	}
}
