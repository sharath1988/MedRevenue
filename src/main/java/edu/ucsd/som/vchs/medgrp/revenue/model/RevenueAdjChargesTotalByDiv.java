/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

/**
 * POJO for Adjustment tab data model
 * 
 * @author somdev5
 *
 */
@Entity
@Table(name="V_TOTAL_BUDGET_ADJ_BY_DIV", catalog="budget")
@NamedStoredProcedureQuery(
		name=RevenueAdjChargesTotalByDiv.GET_TOTALS_BY_DIV_SPROC, procedureName=RevenueAdjChargesTotalByDiv.GET_TOTALS_BY_DIV_SPROC, 
		resultClasses=RevenueAdjChargesTotalByDiv.class,
		parameters={@StoredProcedureParameter(mode=ParameterMode.IN, type=Integer.class, name="pDivisionId")}
)
public class RevenueAdjChargesTotalByDiv {
	
	public static final String GET_TOTALS_BY_DIV_SPROC = "spGetRevenueAdjustmentsTotalByDivision";

	/**
	 * Default constructor 
	 */
	public RevenueAdjChargesTotalByDiv() {
		super();
	}

	@Id
	@Column(name="DIVISION_ID")
	private Integer divisionId;
	
	@Column(name="TOTAL_PROJ_CHARGES")
	private BigDecimal totalProjCharges;
	
	@Column(name="TOTAL_BAD_DEBT_ADJ")
	private BigDecimal totalBadDebtAdj;
	
	@Column(name="TOTAL_BAD_DEBT_ADJ_PERC")
	private BigDecimal totalBadDebtAdjPerc;	
	
	@Column(name="TOTAL_CAP_ADJ")
	private BigDecimal totalCapAdj;
	
	@Column(name="TOTAL_CAP_ADJ_PERC")
	private BigDecimal totalCapAdjPerc;
	
	@Column(name="TOTAL_OTHER_ADJ")
	private BigDecimal totalOtherAdj;
	
	@Column(name="TOTAL_OTHER_ADJ_PERC")
	private BigDecimal totalOtherAdjPerc;
	
	@Column(name="TOTAL_ADJ")
	private BigDecimal totalAdj;
	
	@Column(name="TOTAL_ADJ_PERC")
	private BigDecimal totalAdjPerc;	
	
	@Column(name="TOTAL_ADJ_CHARGES")
	private BigDecimal totalAdjCharges;
	
	@Column(name="TOTAL_CAP_DISTRIBUTION")
	private BigDecimal totalCapDistribution;
	
	@Column(name="TOTAL_EXPECTED_REVENUE")
	private BigDecimal totalExpectedRevenue;

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
	 * @return the totalProjCharges
	 */
	public BigDecimal getTotalProjCharges() {
		return totalProjCharges;
	}

	/**
	 * @param totalProjCharges the totalProjCharges to set
	 */
	public void setTotalProjCharges(BigDecimal totalProjCharges) {
		this.totalProjCharges = totalProjCharges;
	}

	/**
	 * @return the totalBadDebtAdj
	 */
	public BigDecimal getTotalBadDebtAdj() {
		return totalBadDebtAdj;
	}

	/**
	 * @param totalBadDebtAdj the totalBadDebtAdj to set
	 */
	public void setTotalBadDebtAdj(BigDecimal totalBadDebtAdj) {
		this.totalBadDebtAdj = totalBadDebtAdj;
	}

	/**
	 * @return the totalCapAdj
	 */
	public BigDecimal getTotalCapAdj() {
		return totalCapAdj;
	}

	/**
	 * @param totalCapAdj the totalCapAdj to set
	 */
	public void setTotalCapAdj(BigDecimal totalCapAdj) {
		this.totalCapAdj = totalCapAdj;
	}

	/**
	 * @return the totalOtherAdj
	 */
	public BigDecimal getTotalOtherAdj() {
		return totalOtherAdj;
	}

	/**
	 * @param totalOtherAdj the totalOtherAdj to set
	 */
	public void setTotalOtherAdj(BigDecimal totalOtherAdj) {
		this.totalOtherAdj = totalOtherAdj;
	}

	/**
	 * @return the totalAdj
	 */
	public BigDecimal getTotalAdj() {
		return totalAdj;
	}

	/**
	 * @param totalAdj the totalAdj to set
	 */
	public void setTotalAdj(BigDecimal totalAdj) {
		this.totalAdj = totalAdj;
	}

	/**
	 * @return the totalAdjCharges
	 */
	public BigDecimal getTotalAdjCharges() {
		return totalAdjCharges;
	}

	/**
	 * @param totalAdjCharges the totalAdjCharges to set
	 */
	public void setTotalAdjCharges(BigDecimal totalAdjCharges) {
		this.totalAdjCharges = totalAdjCharges;
	}

	/**
	 * @return the totalCapDistribution
	 */
	public BigDecimal getTotalCapDistribution() {
		return totalCapDistribution;
	}

	/**
	 * @param totalCapDistribution the totalCapDistribution to set
	 */
	public void setTotalCapDistribution(BigDecimal totalCapDistribution) {
		this.totalCapDistribution = totalCapDistribution;
	}

	/**
	 * @return the totalExpectedRevenue
	 */
	public BigDecimal getTotalExpectedRevenue() {
		return totalExpectedRevenue;
	}

	/**
	 * @param totalExpectedRevenue the totalExpectedRevenue to set
	 */
	public void setTotalExpectedRevenue(BigDecimal totalExpectedRevenue) {
		this.totalExpectedRevenue = totalExpectedRevenue;
	}
	
	/**
	 * @return the totalBadDebtAdjPerc
	 */
	public BigDecimal getTotalBadDebtAdjPerc() {
		return totalBadDebtAdjPerc;
	}

	/**
	 * @param totalBadDebtAdjPerc the totalBadDebtAdjPerc to set
	 */
	public void setTotalBadDebtAdjPerc(BigDecimal totalBadDebtAdjPerc) {
		this.totalBadDebtAdjPerc = totalBadDebtAdjPerc;
	}

	/**
	 * @return the totalCapAdjPerc
	 */
	public BigDecimal getTotalCapAdjPerc() {
		return totalCapAdjPerc;
	}

	/**
	 * @param totalCapAdjPerc the totalCapAdjPerc to set
	 */
	public void setTotalCapAdjPerc(BigDecimal totalCapAdjPerc) {
		this.totalCapAdjPerc = totalCapAdjPerc;
	}

	/**
	 * @return the totalOtherAdjPerc
	 */
	public BigDecimal getTotalOtherAdjPerc() {
		return totalOtherAdjPerc;
	}

	/**
	 * @param totalOtherAdjPerc the totalOtherAdjPerc to set
	 */
	public void setTotalOtherAdjPerc(BigDecimal totalOtherAdjPerc) {
		this.totalOtherAdjPerc = totalOtherAdjPerc;
	}

	/**
	 * @return the totalAdjPerc
	 */
	public BigDecimal getTotalAdjPerc() {
		return totalAdjPerc;
	}

	/**
	 * @param totalAdjPerc the totalAdjPerc to set
	 */
	public void setTotalAdjPerc(BigDecimal totalAdjPerc) {
		this.totalAdjPerc = totalAdjPerc;
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
		RevenueAdjChargesTotalByDiv other = (RevenueAdjChargesTotalByDiv) obj;
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
		return "RevenueAdjChargesTotalByDiv [divisionId=" + divisionId
				+ ", totalExpectedRevenue=" + totalExpectedRevenue + "]";
	}
}
