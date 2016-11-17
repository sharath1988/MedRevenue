/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author somdev5
 *
 */
@Embeddable
public class RevenueProviderMergePK implements Serializable {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 6692926384727571438L;
		
	@Column(name="source_ein")
	private Integer sourceEin;
	
	@Column(name="target_ein")
	private Integer targetEin;
	
	@Column(name="division_id")
	private Integer divisionId;

	/**
	 * Default constructor
	 */
	public RevenueProviderMergePK() {
		super();
	}

	/**
	 * @return the sourceEin
	 */
	public Integer getSourceEin() {
		return sourceEin;
	}

	/**
	 * @param sourceEin the sourceEin to set
	 */
	public void setSourceEin(Integer sourceEin) {
		this.sourceEin = sourceEin;
	}

	/**
	 * @return the targetEin
	 */
	public Integer getTargetEin() {
		return targetEin;
	}

	/**
	 * @param targetEin the targetEin to set
	 */
	public void setTargetEin(Integer targetEin) {
		this.targetEin = targetEin;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((divisionId == null) ? 0 : divisionId.hashCode());
		result = prime * result
				+ ((sourceEin == null) ? 0 : sourceEin.hashCode());
		result = prime * result
				+ ((targetEin == null) ? 0 : targetEin.hashCode());
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
		RevenueProviderMergePK other = (RevenueProviderMergePK) obj;
		if (divisionId == null) {
			if (other.divisionId != null)
				return false;
		} else if (!divisionId.equals(other.divisionId))
			return false;
		if (sourceEin == null) {
			if (other.sourceEin != null)
				return false;
		} else if (!sourceEin.equals(other.sourceEin))
			return false;
		if (targetEin == null) {
			if (other.targetEin != null)
				return false;
		} else if (!targetEin.equals(other.targetEin))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RevenueProviderMergePK [sourceEin=" + sourceEin
				+ ", targetEin=" + targetEin + ", divisionId=" + divisionId
				+ "]";
	}
}
