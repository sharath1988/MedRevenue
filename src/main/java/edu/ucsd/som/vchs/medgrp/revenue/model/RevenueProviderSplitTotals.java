/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Object for displaying totals at the bottom of the RevenueProvider Split screen
 * 
 * @author somdev5
 *
 */
public class RevenueProviderSplitTotals implements Serializable {
	
	private BigDecimal totalCapPercent = BigDecimal.ZERO;
	
	private BigDecimal totalUnderPercent = BigDecimal.ZERO;
	
	private BigDecimal totalFfsPercent = BigDecimal.ZERO;
	
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -5609777720276845192L;

	/**
	 * Default constructor
	 */
	public RevenueProviderSplitTotals() {
		super();
	}

	/**
	 * @return the totalCapPercent
	 */
	public BigDecimal getTotalCapPercent() {
		return totalCapPercent;
	}

	/**
	 * @param totalCapPercent the totalCapPercent to set
	 */
	public void setTotalCapPercent(BigDecimal totalCapPercent) {
		this.totalCapPercent = totalCapPercent;
	}

	/**
	 * @return the totalUnderPercent
	 */
	public BigDecimal getTotalUnderPercent() {
		return totalUnderPercent;
	}

	/**
	 * @param totalUnderPercent the totalUnderPercent to set
	 */
	public void setTotalUnderPercent(BigDecimal totalUnderPercent) {
		this.totalUnderPercent = totalUnderPercent;
	}

	/**
	 * @return the totalFfsPercent
	 */
	public BigDecimal getTotalFfsPercent() {
		return totalFfsPercent;
	}

	/**
	 * @param totalFfsPercent the totalFfsPercent to set
	 */
	public void setTotalFfsPercent(BigDecimal totalFfsPercent) {
		this.totalFfsPercent = totalFfsPercent;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((totalCapPercent == null) ? 0 : totalCapPercent.hashCode());
		result = prime * result
				+ ((totalFfsPercent == null) ? 0 : totalFfsPercent.hashCode());
		result = prime
				* result
				+ ((totalUnderPercent == null) ? 0 : totalUnderPercent
						.hashCode());
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
		RevenueProviderSplitTotals other = (RevenueProviderSplitTotals) obj;
		if (totalCapPercent == null) {
			if (other.totalCapPercent != null)
				return false;
		} else if (!totalCapPercent.equals(other.totalCapPercent))
			return false;
		if (totalFfsPercent == null) {
			if (other.totalFfsPercent != null)
				return false;
		} else if (!totalFfsPercent.equals(other.totalFfsPercent))
			return false;
		if (totalUnderPercent == null) {
			if (other.totalUnderPercent != null)
				return false;
		} else if (!totalUnderPercent.equals(other.totalUnderPercent))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RevenueProviderSplitTotals [totalCapPercent=" + totalCapPercent
				+ ", totalUnderPercent=" + totalUnderPercent
				+ ", totalFfsPercent=" + totalFfsPercent + "]";
	}
}
