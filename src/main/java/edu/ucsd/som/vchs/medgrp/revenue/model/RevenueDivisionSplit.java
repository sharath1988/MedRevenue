/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * POJO for holding Revenue split percentages at the division level
 * 
 * TODO verify division 22 on web ui
 * 
 * @author somdev5
 *
 */
@Entity
@Table(name="v_revenue_prior_actual_split_perc_by_category_and_div", catalog="budget")
public class RevenueDivisionSplit implements Serializable {
	
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -3304455525644977100L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="py_actual_id")
	private Integer priorYearActualId;
	
	@Column(name="division_id")
	private Integer divisionId;
	
	@Column(name="sos")
	private String sos;
	
	@Column(name="total_wrvus_amount")
	private BigDecimal totalWrvusAmt;
	
	@Column(name="total_cap_wrvus")
	private BigDecimal totalCapWrvus;
	
	@Column(name="cap_wrvus_split_percentage")
	private BigDecimal capSplitPercentage;
	
	@Column(name="total_under_wrvus")
	private BigDecimal totalUnderWrvus;
	
	@Column(name="under_wrvus_split_percentage")
	private BigDecimal underSplitPercentage;
	
	@Column(name="total_ffs_wrvus")
	private BigDecimal totalFfsWrvus;
	
	@Column(name="ffs_wrvus_split_percentage")
	private BigDecimal ffsSplitPercentage;
	
	/**
	 * Default constructor
	 */
	public RevenueDivisionSplit() {
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
	 * @return the totalWrvusAmt
	 */
	public BigDecimal getTotalWrvusAmt() {
		return totalWrvusAmt;
	}

	/**
	 * @param totalWrvusAmt the totalWrvusAmt to set
	 */
	public void setTotalWrvusAmt(BigDecimal totalWrvusAmt) {
		this.totalWrvusAmt = totalWrvusAmt;
	}

	/**
	 * @return the totalCapWrvus
	 */
	public BigDecimal getTotalCapWrvus() {
		return totalCapWrvus;
	}

	/**
	 * @param totalCapWrvus the totalCapWrvus to set
	 */
	public void setTotalCapWrvus(BigDecimal totalCapWrvus) {
		this.totalCapWrvus = totalCapWrvus;
	}

	/**
	 * @return the capSplitPercentage
	 */
	public BigDecimal getCapSplitPercentage() {
		return capSplitPercentage;
	}

	/**
	 * @param capSplitPercentage the capSplitPercentage to set
	 */
	public void setCapSplitPercentage(BigDecimal capSplitPercentage) {
		this.capSplitPercentage = capSplitPercentage;
	}

	/**
	 * @return the totalUnderWrvus
	 */
	public BigDecimal getTotalUnderWrvus() {
		return totalUnderWrvus;
	}

	/**
	 * @param totalUnderWrvus the totalUnderWrvus to set
	 */
	public void setTotalUnderWrvus(BigDecimal totalUnderWrvus) {
		this.totalUnderWrvus = totalUnderWrvus;
	}

	/**
	 * @return the underSplitPercentage
	 */
	public BigDecimal getUnderSplitPercentage() {
		return underSplitPercentage;
	}

	/**
	 * @param underSplitPercentage the underSplitPercentage to set
	 */
	public void setUnderSplitPercentage(BigDecimal underSplitPercentage) {
		this.underSplitPercentage = underSplitPercentage;
	}

	/**
	 * @return the totalFfsWrvus
	 */
	public BigDecimal getTotalFfsWrvus() {
		return totalFfsWrvus;
	}

	/**
	 * @param totalFfsWrvus the totalFfsWrvus to set
	 */
	public void setTotalFfsWrvus(BigDecimal totalFfsWrvus) {
		this.totalFfsWrvus = totalFfsWrvus;
	}

	/**
	 * @return the ffsSplitPercentage
	 */
	public BigDecimal getFfsSplitPercentage() {
		return ffsSplitPercentage;
	}

	/**
	 * @param ffsSplitPercentage the ffsSplitPercentage to set
	 */
	public void setFfsSplitPercentage(BigDecimal ffsSplitPercentage) {
		this.ffsSplitPercentage = ffsSplitPercentage;
	}

	/**
	 * @return the priorYearActualId
	 */
	public Integer getPriorYearActualId() {
		return priorYearActualId;
	}

	/**
	 * @param priorYearActualId the priorYearActualId to set
	 */
	public void setPriorYearActualId(Integer priorYearActualId) {
		this.priorYearActualId = priorYearActualId;
	}

	/**
	 * @return the sos
	 */
	public String getSos() {
		return sos;
	}

	/**
	 * @param sos the sos to set
	 */
	public void setSos(String sos) {
		this.sos = sos;
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
		result = prime * result + ((sos == null) ? 0 : sos.hashCode());
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
		RevenueDivisionSplit other = (RevenueDivisionSplit) obj;
		if (divisionId == null) {
			if (other.divisionId != null)
				return false;
		} else if (!divisionId.equals(other.divisionId))
			return false;
		if (sos == null) {
			if (other.sos != null)
				return false;
		} else if (!sos.equals(other.sos))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RevenueDivisionSplit [priorYearActualId=" + priorYearActualId
				+ ", divisionId=" + divisionId + ", sos=" + sos
				+ ", totalWrvusAmt=" + totalWrvusAmt + ", totalCapWrvus="
				+ totalCapWrvus + ", capSplitPercentage=" + capSplitPercentage
				+ ", totalUnderWrvus=" + totalUnderWrvus
				+ ", underSplitPercentage=" + underSplitPercentage
				+ ", totalFfsWrvus=" + totalFfsWrvus + ", ffsSplitPercentage="
				+ ffsSplitPercentage + "]";
	}
}
