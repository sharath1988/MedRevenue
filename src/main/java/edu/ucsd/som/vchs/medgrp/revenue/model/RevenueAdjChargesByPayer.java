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
@Table(name="V_ADJ_CHARGES_BY_PL_CATEGORY", catalog="budget")
@NamedStoredProcedureQuery(
		name=RevenueAdjChargesByPayer.GET_REVENUE_ADJ_CHARGES_BY_PL_PROC, procedureName=RevenueAdjChargesByPayer.GET_REVENUE_ADJ_CHARGES_BY_PL_PROC, 
		resultClasses=RevenueAdjChargesByPayer.class,
		parameters={@StoredProcedureParameter(mode=ParameterMode.IN, type=Integer.class, name="pDivisionId")}
)
public class RevenueAdjChargesByPayer {
	
	public static final String GET_REVENUE_ADJ_CHARGES_BY_PL_PROC = "spGetRevenueAdjustmentsByDivision"; 

	/**
	 * Default constructor 
	 */
	public RevenueAdjChargesByPayer() {
		super();
	}

	@Id
	@Column(name="id")
	private String id;
	
	@Column(name="division_id")
	private Integer divisionId;
	
	@Column(name="total_proj_charges")
	private BigDecimal totalProjCharges;
	
	@Column(name="PL_CHARGE_PERC")
	private BigDecimal plChargePerc;
	
	@Column(name="PL_CATEGORY")
	private String plCategory;
	
	@Column(name="total_proj_pl_charges")
	private BigDecimal totalProjPlCharges;
	
	@Column(name="BAD_DEBT_ADJ_PERC")
	private BigDecimal badDebtAdjPerc;
	
	@Column(name="BAD_DEBT_ADJ")
	private BigDecimal badDebtAdj;
	
	@Column(name="CAPITATION_ADJ_PERC")
	private BigDecimal capitationAdjPerc;
	
	@Column(name="CAP_ADJ")
	private BigDecimal capitationAdj;
	
	@Column(name="OTHER_ADJ_PERC")
	private BigDecimal otherAdjPerc;
	
	@Column(name="OTHER_ADJ")
	private BigDecimal otherAdj;
	
	@Column(name="TOTAL_ADJ")
	private BigDecimal totalAdj;
	
	@Column(name="TOTAL_ADJ_CHARGES")
	private BigDecimal totalAdjCharges;

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
	 * @return the plCategory
	 */
	public String getPlCategory() {
		return plCategory;
	}

	/**
	 * @param plCategory the plCategory to set
	 */
	public void setPlCategory(String plCategory) {
		this.plCategory = plCategory;
	}

	/**
	 * @return the totalProjPlCharges
	 */
	public BigDecimal getTotalProjPlCharges() {
		return totalProjPlCharges;
	}

	/**
	 * @param totalProjPlCharges the totalProjPlCharges to set
	 */
	public void setTotalProjPlCharges(BigDecimal totalProjPlCharges) {
		this.totalProjPlCharges = totalProjPlCharges;
	}

	/**
	 * @return the badDebtAdjPerc
	 */
	public BigDecimal getBadDebtAdjPerc() {
		return badDebtAdjPerc;
	}

	/**
	 * @param badDebtAdjPerc the badDebtAdjPerc to set
	 */
	public void setBadDebtAdjPerc(BigDecimal badDebtAdjPerc) {
		this.badDebtAdjPerc = badDebtAdjPerc;
	}

	/**
	 * @return the badDebtAdj
	 */
	public BigDecimal getBadDebtAdj() {
		return badDebtAdj;
	}

	/**
	 * @param badDebtAdj the badDebtAdj to set
	 */
	public void setBadDebtAdj(BigDecimal badDebtAdj) {
		this.badDebtAdj = badDebtAdj;
	}

	/**
	 * @return the capitationAdjPerc
	 */
	public BigDecimal getCapitationAdjPerc() {
		return capitationAdjPerc;
	}

	/**
	 * @param capitationAdjPerc the capitationAdjPerc to set
	 */
	public void setCapitationAdjPerc(BigDecimal capitationAdjPerc) {
		this.capitationAdjPerc = capitationAdjPerc;
	}

	/**
	 * @return the capitationAdj
	 */
	public BigDecimal getCapitationAdj() {
		return capitationAdj;
	}

	/**
	 * @param capitationAdj the capitationAdj to set
	 */
	public void setCapitationAdj(BigDecimal capitationAdj) {
		this.capitationAdj = capitationAdj;
	}

	/**
	 * @return the otherAdjPerc
	 */
	public BigDecimal getOtherAdjPerc() {
		return otherAdjPerc;
	}

	/**
	 * @param otherAdjPerc the otherAdjPerc to set
	 */
	public void setOtherAdjPerc(BigDecimal otherAdjPerc) {
		this.otherAdjPerc = otherAdjPerc;
	}

	/**
	 * @return the otherAdj
	 */
	public BigDecimal getOtherAdj() {
		return otherAdj;
	}

	/**
	 * @param otherAdj the otherAdj to set
	 */
	public void setOtherAdj(BigDecimal otherAdj) {
		this.otherAdj = otherAdj;
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
	 * @return the plChargePerc
	 */
	public BigDecimal getPlChargePerc() {
		return plChargePerc;
	}

	/**
	 * @param plChargePerc the plChargePerc to set
	 */
	public void setPlChargePerc(BigDecimal plChargePerc) {
		this.plChargePerc = plChargePerc;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		RevenueAdjChargesByPayer other = (RevenueAdjChargesByPayer) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RevenueAdjChargesByPayer [divisionId=" + divisionId
				+ ", plCategory=" + plCategory + ", totalAdj=" + totalAdj
				+ ", totalAdjCharges=" + totalAdjCharges + "]";
	}
}
